/**
 * File Name: GetFavQuestionServiceImpl.java
 * Description: 获取用户收藏题目列表服务实现类
 * Author: holic512
 * Created Date: 2025-04-29
 * Version: 1.0
 * Usage:
 * 提供用户获取收藏列表功能实现
 */
package org.example.servicequestion.user.favQuestion.service.Impl;

import org.example.servicequestion.config.Redis.RedisKey;
import org.example.servicequestion.config.Redis.service.RedisZSetService;
import org.example.servicequestion.user.commonService.IdConversionService;
import org.example.servicequestion.user.favQuestion.dto.FavQuestionDTO;
import org.example.servicequestion.user.favQuestion.dto.FavQuestionListDTO;
import org.example.servicequestion.user.favQuestion.mapper.FavQUserFavoriteQuestionMapper;
import org.example.servicequestion.user.favQuestion.service.GetFavQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class GetFavQuestionServiceImpl implements GetFavQuestionService {

    private final RedisZSetService redisZSetService;
    private final FavQUserFavoriteQuestionMapper favQUserFavoriteQuestionMapper;
    private final IdConversionService idConversionService;

    @Autowired
    public GetFavQuestionServiceImpl(RedisZSetService redisZSetService,
                                    FavQUserFavoriteQuestionMapper favQUserFavoriteQuestionMapper,
                                    IdConversionService idConversionService) {
        this.redisZSetService = redisZSetService;
        this.favQUserFavoriteQuestionMapper = favQUserFavoriteQuestionMapper;
        this.idConversionService = idConversionService;
    }

    @Override
    public FavQuestionListDTO getUserFavQuestions(Long userId, int page, int pageSize, String title) {
        // 参数校验
        if (userId == null || page < 1 || pageSize < 1) {
            return new FavQuestionListDTO(Collections.emptyList(), 0, 0, page);
        }
        
        // 如果提供了标题搜索，直接从数据库搜索
        if (StringUtils.hasText(title)) {
            return searchFavQuestionsByTitle(userId, page, pageSize, title);
        }
        
        // 计算分页参数
        int start = (page - 1) * pageSize;
        int end = start + pageSize - 1;
        
        // 构建Redis键
        String redisKey = RedisKey.FAV_QUESTION_USER_KEY + userId;
        
        // 从Redis ZSet中获取数据（按时间戳倒序）
        Set<Object> questionIds = redisZSetService.rangeFromZSet(redisKey, 0, -1);
        
        if (questionIds != null && !questionIds.isEmpty()) {
            // Redis中有数据，但我们需要题目标题信息，所以还需从数据库获取
            List<FavQuestionDTO> favQuestionDTOs = loadFavQuestionsWithDetail(userId);
            
            if (favQuestionDTOs.isEmpty()) {
                return new FavQuestionListDTO(Collections.emptyList(), 0, 0, page);
            }
            
            // 计算总页数
            int totalCount = favQuestionDTOs.size();
            int totalPages = (totalCount + pageSize - 1) / pageSize;
            
            // 处理虚拟ID
            for (FavQuestionDTO dto : favQuestionDTOs) {
                String virtualId = idConversionService.getVirtualIdFromOriginalId(dto.getQuestionId());
                dto.setVirtualId(virtualId);
                // 清空真实ID，不返回给前端
                dto.setQuestionId(null);
            }
            
            // 分页处理
            List<FavQuestionDTO> pagedList = start < favQuestionDTOs.size() ? 
                    favQuestionDTOs.subList(start, Math.min(end + 1, favQuestionDTOs.size())) : 
                    Collections.emptyList();
            
            return new FavQuestionListDTO(pagedList, totalCount, totalPages, page);
        } else {
            // Redis中没有数据，从数据库中查询
            List<FavQuestionDTO> dbList = loadFavQuestionsWithDetail(userId);
            
            if (dbList.isEmpty()) {
                // 数据库中也没有数据
                return new FavQuestionListDTO(Collections.emptyList(), 0, 0, page);
            }
            
            // 处理虚拟ID
            for (FavQuestionDTO dto : dbList) {
                String virtualId = idConversionService.getVirtualIdFromOriginalId(dto.getQuestionId());
                dto.setVirtualId(virtualId);
                // 清空真实ID，不返回给前端
                dto.setQuestionId(null);
            }
            
            // 添加到Redis ZSet中
            for (FavQuestionDTO dto : dbList) {
                try {
                    if (dto.getCreateTime() != null && dto.getQuestionId() != null) {
                        long timestamp = dto.getCreateTime().atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
                        redisZSetService.addToZSetWithExpire(redisKey, dto.getQuestionId(), timestamp, 600);
                    }
                } catch (Exception e) {
                    System.err.println("处理收藏数据时出错: " + e.getMessage());
                }
            }
            
            // 计算总页数
            int totalCount = dbList.size();
            int totalPages = (totalCount + pageSize - 1) / pageSize;
            
            // 分页处理
            List<FavQuestionDTO> pagedList = start < dbList.size() ? 
                    dbList.subList(start, Math.min(end + 1, dbList.size())) : 
                    Collections.emptyList();
            
            return new FavQuestionListDTO(pagedList, totalCount, totalPages, page);
        }
    }
    
    @Override
    public boolean checkUserFavorite(Long userId, String virtualId) {
        // 参数校验
        if (userId == null || !StringUtils.hasText(virtualId)) {
            return false;
        }
        
        try {
            // 从虚拟ID获取真实ID
            Long questionId = idConversionService.getOriginalIdFromVirtualId(virtualId);
            if (questionId == null) {
                return false;
            }
            
            // 先查询Redis
            String redisKey = RedisKey.FAV_QUESTION_USER_KEY + userId;
            Double score = redisZSetService.getScoreFromZSet(redisKey, questionId);
            
            // Redis中找到了，说明已收藏
            if (score != null) {
                return true;
            }
            
            // Redis中没有，查询数据库
            Integer count = favQUserFavoriteQuestionMapper.checkUserFavorite(userId, questionId);
            return count != null && count > 0;
            
        } catch (Exception e) {
            System.err.println("检查收藏状态时出错: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据标题搜索收藏题目并分页
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页大小
     * @param title    搜索标题
     * @return 收藏题目列表DTO
     */
    private FavQuestionListDTO searchFavQuestionsByTitle(Long userId, int page, int pageSize, String title) {
        List<FavQuestionDTO> searchResults = favQUserFavoriteQuestionMapper.searchUserFavQuestionsByTitle(userId, title);
        
        if (searchResults.isEmpty()) {
            return new FavQuestionListDTO(Collections.emptyList(), 0, 0, page);
        }
        
        // 处理虚拟ID
        for (FavQuestionDTO dto : searchResults) {
            String virtualId = idConversionService.getVirtualIdFromOriginalId(dto.getQuestionId());
            dto.setVirtualId(virtualId);
            // 清空真实ID，不返回给前端
            dto.setQuestionId(null);
        }
        
        // 计算总页数
        int totalCount = searchResults.size();
        int totalPages = (totalCount + pageSize - 1) / pageSize;
        
        // 计算分页参数
        int start = (page - 1) * pageSize;
        int end = start + pageSize - 1;
        
        // 分页处理
        List<FavQuestionDTO> pagedList = start < searchResults.size() ? 
                searchResults.subList(start, Math.min(end + 1, searchResults.size())) : 
                Collections.emptyList();
        
        return new FavQuestionListDTO(pagedList, totalCount, totalPages, page);
    }
    
    /**
     * 从数据库加载用户收藏题目详情列表
     *
     * @param userId 用户ID
     * @return 收藏题目详情列表
     */
    private List<FavQuestionDTO> loadFavQuestionsWithDetail(Long userId) {
        return favQUserFavoriteQuestionMapper.selectUserFavQuestionsDetail(userId);
    }
} 