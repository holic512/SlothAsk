/**
 * File Name: GetQuestionCommentServiceImpl.java
 * Description: 获取问题评论服务实现类
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.QuestionComment;
import org.example.servicequestion.entity.UserProfile;
import org.example.servicequestion.feign.ServiceImageFeign;
import org.example.servicequestion.user.questionComment.dto.CommentDTO;
import org.example.servicequestion.user.questionComment.dto.CommentListResponseDTO;
import org.example.servicequestion.user.questionComment.dto.CommentQueryDTO;
import org.example.servicequestion.user.questionComment.dto.UserInfoDTO;
import org.example.servicequestion.user.questionComment.mapper.QCQuestionCommentLikeMapper;
import org.example.servicequestion.user.questionComment.mapper.QCQuestionCommentMapper;
import org.example.servicequestion.user.questionComment.mapper.QCUserProfileMapper;
import org.example.servicequestion.user.questionComment.service.GetQuestionCommentService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GetQuestionCommentServiceImpl implements GetQuestionCommentService {

    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";
    private final QCQuestionCommentMapper commentMapper;
    private final QCUserProfileMapper userProfileMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final QCQuestionCommentLikeMapper commentLikeMapper;
    private final ServiceImageFeign serviceImageFeign;

    @Autowired
    public GetQuestionCommentServiceImpl(QCQuestionCommentMapper commentMapper,
                                         QCUserProfileMapper userProfileMapper,
                                         RedisTemplate<String, Object> redisTemplate,
                                         QCQuestionCommentLikeMapper commentLikeMapper,
                                         ServiceImageFeign serviceImageFeign) {
        this.commentMapper = commentMapper;
        this.userProfileMapper = userProfileMapper;
        this.redisTemplate = redisTemplate;
        this.commentLikeMapper = commentLikeMapper;
        this.serviceImageFeign = serviceImageFeign;
    }

    @Override
    public CommentListResponseDTO getCommentsByVirtualId(CommentQueryDTO queryDTO, Long user_id) {
        // 获取原始ID
        Long questionId = getOriginalIdFromVirtualId(queryDTO.getTargetVirtualId());
        if (questionId == null) {
            return null;
        }

        // 创建对象
        CommentListResponseDTO responseDTO = new CommentListResponseDTO();
        responseDTO.setTotal(0L);
        responseDTO.setTotalPages(0);
        responseDTO.setCurrentPage(queryDTO.getPageNum());
        responseDTO.setComments(new ArrayList<>());

        // 设置排序
        String orderColumn = queryDTO.getOrderBy() == 1 ? "like_count" : "create_time";
        String orderDirection = queryDTO.getOrderDirection() == 1 ? "ASC" : "DESC";

        // 创建分页对象
        Page<QuestionComment> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 创建查询条件
        LambdaQueryWrapper<QuestionComment> queryWrapper = new LambdaQueryWrapper<QuestionComment>()
                .eq(QuestionComment::getQuestionId, questionId)
                .orderByDesc(orderColumn.equals("create_time") && orderDirection.equals("DESC"), QuestionComment::getCreateTime)
                .orderByAsc(orderColumn.equals("create_time") && orderDirection.equals("ASC"), QuestionComment::getCreateTime)
                .orderByDesc(orderColumn.equals("like_count") && orderDirection.equals("DESC"), QuestionComment::getLikeCount)
                .orderByAsc(orderColumn.equals("like_count") && orderDirection.equals("ASC"), QuestionComment::getLikeCount);

        // 查询评论列表
        IPage<QuestionComment> commentPage = commentMapper.selectPage(page, queryWrapper);

        // 获取所有用户ID
        List<Long> userIds = commentPage.getRecords().stream()
                .map(QuestionComment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询用户信息
        Map<Long, UserProfile> userProfileMap = getUserProfiles(userIds);


        Set<Long> likedCommentIdSet;
        // 当用户id不为空
        if (user_id != null) {
            // 提取评论ID列表
            List<Long> commentIds = commentPage.getRecords().stream()
                    .map(QuestionComment::getId)
                    .collect(Collectors.toList());

            // 如果评论 ID 为空，提前初始化一个空的 likedCommentIdSet
            if (commentIds.isEmpty()) {
                likedCommentIdSet = new HashSet<>();
            } else {
                // 查询该用户点赞了哪些评论
                List<Long> likedCommentIds = commentLikeMapper.selectLikedCommentIds(user_id, commentIds);
                likedCommentIdSet = new HashSet<>(likedCommentIds); // 这里是赋值，不是重新声明
            }
        } else {
            likedCommentIdSet = new HashSet<>();
        }


        // 转换为DTO
        List<CommentDTO> commentDTOs = commentPage.getRecords().stream()
                .map(comment -> convertToDTO(comment, userProfileMap, user_id, likedCommentIdSet))
                .collect(Collectors.toList());

        // 组装返回结果
        responseDTO.setTotal(commentPage.getTotal());
        responseDTO.setTotalPages((int) commentPage.getPages());
        responseDTO.setCurrentPage(queryDTO.getPageNum());
        responseDTO.setComments(commentDTOs);

        return responseDTO;
    }

    private Map<Long, UserProfile> getUserProfiles(List<Long> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }

        // 查询用户资料
        LambdaQueryWrapper<UserProfile> queryWrapper = new LambdaQueryWrapper<UserProfile>()
                .in(UserProfile::getUserId, userIds)
                .select(UserProfile::getUserId, UserProfile::getNickname, UserProfile::getAvatar);

        List<UserProfile> userProfiles = userProfileMapper.selectList(queryWrapper);

        // 收集所有以 # 开头的头像文件名
        Map<String, UserProfile> avatarToUserMap = new HashMap<>();
        for (UserProfile profile : userProfiles) {
            String avatar = profile.getAvatar();
            if (avatar != null && avatar.startsWith("#")) {
                String fileName = avatar.substring(1);
                avatarToUserMap.put(fileName, profile);
            }
        }

        // 如果有需要处理的头像，批量获取真实 URL
        if (!avatarToUserMap.isEmpty()) {
            List<String> fileNames = new ArrayList<>(avatarToUserMap.keySet());

            // 远程批量获取图片 URL
            Map<String, String> realUrls = serviceImageFeign.getPreviewUrls(fileNames);

            // 更新头像地址
            for (Map.Entry<String, String> entry : realUrls.entrySet()) {
                UserProfile profile = avatarToUserMap.get(entry.getKey());
                if (profile != null) {
                    profile.setAvatar(entry.getValue());
                }
            }
        }

        return userProfiles.stream()
                .collect(Collectors.toMap(UserProfile::getUserId, profile -> profile));
    }


    /**
     * 将评论实体转换为DTO
     */
    private CommentDTO convertToDTO(QuestionComment comment, Map<Long, UserProfile> userProfileMap, Long user_id, Set<Long> likedCommentIdSet) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setParentId(comment.getParentId());
        dto.setLikeCount(comment.getLikeCount());
        dto.setCreateTime(comment.getCreateTime());

        // 设置用户信息
        UserInfoDTO userInfo = new UserInfoDTO();
        UserProfile profile = userProfileMap.get(comment.getUserId());
        if (profile != null) {
            userInfo.setNickname(profile.getNickname());
            userInfo.setAvatar(profile.getAvatar());
        }
        dto.setUserInfo(userInfo);

        // 当用户id不为空
        if (user_id != null) {
            // 设置此次接口访问者是否是该评论作者
            if (comment.getUserId().equals(user_id)) {
                dto.setIsAuthor(1);
            }
            // 检测此次评论用户是否点赞
            dto.setIsLike(likedCommentIdSet.contains(comment.getId()) ? 1 : 0);
        }


        return dto;
    }

    // 根据VirtualId获取原始ID
    private Long getOriginalIdFromVirtualId(String virtualId) {
        String originalIdStr = (String) redisTemplate.opsForValue().get(VidKey + virtualId);
        Long originalId = null;

        if (originalIdStr != null) {
            // 清理潜在的双引号（如 Redis 中存的是 "\"1\""）
            String cleaned = originalIdStr.replaceAll("^\"|\"$", "");
            if (cleaned.matches("\\d+")) {
                originalId = Long.parseLong(cleaned);
            }
        }

        if (originalId == null) {
            originalId = IdEncryptor.decryptId(virtualId);
            if (originalId != null) {
                redisTemplate.opsForValue().set(VidKey + virtualId, String.valueOf(originalId), 1, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(VidKey + originalId, virtualId, 1, TimeUnit.DAYS);
            }
        }

        return originalId;
    }
}
