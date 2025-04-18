/**
 * File Name: GetUserHistoryServiceImpl.java
 * Description: 获取用户历史服务实现类
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.service.Impl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.user.history.dto.HistoryQuestionDTO;
import org.example.servicequestion.user.history.dto.TagSimpleDTO;
import org.example.servicequestion.user.history.dto.UserHistoryDTO;
import org.example.servicequestion.user.history.mapper.UserHistoryTagMapper;
import org.example.servicequestion.user.history.mapper.UserHistoryUserQuestionHistoryMapper;
import org.example.servicequestion.user.history.service.GetUserHistoryService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetUserHistoryServiceImpl implements GetUserHistoryService {

    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";

    private final UserHistoryUserQuestionHistoryMapper userHistoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserHistoryTagMapper userHistoryTagMapper;

    @Autowired
    public GetUserHistoryServiceImpl(
            UserHistoryUserQuestionHistoryMapper userHistoryMapper,
            RedisTemplate<String, Object> redisTemplate, UserHistoryTagMapper userHistoryTagMapper) {
        this.userHistoryMapper = userHistoryMapper;
        this.redisTemplate = redisTemplate;
        this.userHistoryTagMapper = userHistoryTagMapper;
    }

    @Override
    public HistoryQuestionDTO getUserHistory(Long userId, int page, int pageSize) {
        // 参数校验
        if (userId == null || userId <= 0) {
            return null;
        }

        // 页码从1开始，需要转换为偏移量
        if (page < 1) {
            page = 1;
        }
        int offset = (page - 1) * pageSize;

        // 从数据库获取历史记录
        List<UserHistoryDTO> historyList = userHistoryMapper.getUserHistoryList(userId, offset, pageSize);

        // 获取标签列表
        List<TagSimpleDTO> tags = userHistoryTagMapper.selectIdAndNameByIds(extractTagIds(historyList));


        // 处理虚拟ID
        for (UserHistoryDTO history : historyList) {
            Long questionId = Long.valueOf(history.getVirtualId()); // 此时DTO中的virtualId实际上是原始questionId

            // 尝试从缓存获取虚拟ID
            String questionVirtualId = (String) redisTemplate.opsForValue().get(VidKey + questionId);

            if (questionVirtualId == null) {
                // 如果缓存中没有，则生成新的虚拟ID
                questionVirtualId = IdEncryptor.encryptId(questionId);

                // 双向缓存虚拟ID和原始ID的映射关系
                redisTemplate.opsForValue().set(VidKey + questionId, questionVirtualId, 1, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(VidKey + questionVirtualId, String.valueOf(questionId), 1, TimeUnit.DAYS);
            }

            // 设置虚拟ID
            history.setVirtualId(questionVirtualId);
        }

        return new HistoryQuestionDTO(historyList, tags);
    }

    // 获取当前列表的 tags 集合
    public Set<Integer> extractTagIds(List<UserHistoryDTO> userHistoryList) {
        return userHistoryList.stream() // 遍历每个 UserHistoryDTO 对象
                .map(UserHistoryDTO::getTagCategoryIds) // 获取每个 UserHistoryDTO 的 tagCategoryIds 字符串
                .flatMap(tagCategoryIds -> Arrays.stream(tagCategoryIds.split(" "))) // 按空格拆分标签分类
                .flatMap(tag -> Arrays.stream(tag.substring(1, tag.length() - 1).split(","))) // 去除括号并拆分
                .filter(tagId -> tagId.matches("\\d+")) // 过滤掉非数字的标签
                .map(Integer::parseInt) // 转换为 Integer 类型
                .collect(Collectors.toSet()); // 收集到 Set 中，去重
    }

}
