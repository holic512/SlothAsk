/**
 * File Name: PostQuestionCommentServiceImpl.java
 * Description: 问题评论服务实现类，处理评论的增删改查及相关业务逻辑
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 * Usage:
 * - 实现评论的添加、点赞/取消点赞、删除等核心功能
 * - 处理评论树形结构的级联删除
 * - 使用Redis缓存虚拟ID与原始ID的映射关系
 * - 提供完整的评论业务逻辑处理和状态返回
 */
package org.example.servicequestion.user.questionComment.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.servicequestion.config.Redis.RedisConfig;
import org.example.servicequestion.entity.QuestionComment;
import org.example.servicequestion.entity.QuestionCommentLike;
import org.example.servicequestion.user.questionComment.dto.CommentRequestDTO;
import org.example.servicequestion.user.questionComment.enums.PostCommentStatusEnum;
import org.example.servicequestion.user.questionComment.mapper.QCQuestionCommentLikeMapper;
import org.example.servicequestion.user.questionComment.mapper.QCQuestionCommentMapper;
import org.example.servicequestion.user.questionComment.service.PostQuestionCommentService;
import org.example.servicequestion.util.IdEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@Service
public class PostQuestionCommentServiceImpl implements PostQuestionCommentService {

    private final static String VidKey = RedisConfig.getKey() + "Question:VId:";
    private final QCQuestionCommentMapper commentMapper;
    private final QCQuestionCommentLikeMapper commentLikeMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PostQuestionCommentServiceImpl(QCQuestionCommentMapper commentMapper,
                                          QCQuestionCommentLikeMapper commentLikeMapper,
                                          RedisTemplate<String, Object> redisTemplate) {
        this.commentMapper = commentMapper;
        this.commentLikeMapper = commentLikeMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public PostCommentStatusEnum addComment(Long userId, CommentRequestDTO requestDTO) {
        // 检查用户是否登录
        if (userId == null || userId <= 0) {
            return PostCommentStatusEnum.USER_NOT_LOGGED_IN;
        }

        // 检查评论内容是否为空
        if (!StringUtils.hasText(requestDTO.getContent())) {
            return PostCommentStatusEnum.EMPTY_CONTENT;
        }

        // 解析虚拟ID为原始ID
        Long targetId = getOriginalIdFromVirtualId(requestDTO.getTargetVirtualId());
        if (targetId == null) {
            return PostCommentStatusEnum.INVALID_TARGET_ID;
        }

        try {
            // 创建评论实体
            QuestionComment comment = new QuestionComment();
            comment.setUserId(userId);
            comment.setQuestionId(targetId);
            comment.setContent(requestDTO.getContent());
            comment.setParentId(requestDTO.getParentId());
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());
            comment.setLikeCount(0);

            // 保存评论
            commentMapper.insert(comment);

            return PostCommentStatusEnum.SUCCESS;
        } catch (Exception e) {
            return PostCommentStatusEnum.SYSTEM_ERROR;
        }
    }

    @Override
    public PostCommentStatusEnum likeComment(Long userId, Long commentId) {
        // 检查用户是否登录
        if (userId == null || userId <= 0) {
            return PostCommentStatusEnum.USER_NOT_LOGGED_IN;
        }

        try {
            // 检查评论是否存在
            QuestionComment comment = commentMapper.selectById(commentId);
            if (comment == null) {
                return PostCommentStatusEnum.INVALID_TARGET_ID;
            }

            // 检查是否已经点赞
            LambdaQueryWrapper<QuestionCommentLike> queryWrapper = new LambdaQueryWrapper<QuestionCommentLike>()
                    .eq(QuestionCommentLike::getUserId, userId)
                    .eq(QuestionCommentLike::getCommentId, commentId);
            
            QuestionCommentLike like = commentLikeMapper.selectOne(queryWrapper);
            if (like != null) {
                // 已经点赞，返回成功
                return PostCommentStatusEnum.SUCCESS;
            }

            // 创建点赞记录
            QuestionCommentLike commentLike = new QuestionCommentLike();
            commentLike.setCommentId(commentId);
            commentLike.setUserId(userId);
            commentLike.setCreateTime(LocalDateTime.now());

            // 保存点赞记录
            commentLikeMapper.insert(commentLike);

            return PostCommentStatusEnum.SUCCESS;
        } catch (Exception e) {
            return PostCommentStatusEnum.SYSTEM_ERROR;
        }
    }

    @Override
    public PostCommentStatusEnum unlikeComment(Long userId, Long commentId) {
        // 检查用户是否登录
        if (userId == null || userId <= 0) {
            return PostCommentStatusEnum.USER_NOT_LOGGED_IN;
        }

        try {
            // 查询点赞记录
            LambdaQueryWrapper<QuestionCommentLike> queryWrapper = new LambdaQueryWrapper<QuestionCommentLike>()
                    .eq(QuestionCommentLike::getUserId, userId)
                    .eq(QuestionCommentLike::getCommentId, commentId);
            
            // 删除点赞记录
            commentLikeMapper.delete(queryWrapper);

            return PostCommentStatusEnum.SUCCESS;
        } catch (Exception e) {
            return PostCommentStatusEnum.SYSTEM_ERROR;
        }
    }

    @Override
    public PostCommentStatusEnum deleteComment(Long userId, Long commentId) {
        // 检查用户是否登录
        if (userId == null || userId <= 0) {
            return PostCommentStatusEnum.USER_NOT_LOGGED_IN;
        }

        // 检查评论ID是否有效
        if (commentId == null) {
            return PostCommentStatusEnum.INVALID_TARGET_ID;
        }

        try {
            // 检查评论是否存在
            QuestionComment comment = commentMapper.selectById(commentId);
            if (comment == null) {
                return PostCommentStatusEnum.COMMENT_NOT_FOUND;
            }

            // 检查是否是评论作者（权限检查）
            if (!comment.getUserId().equals(userId)) {
                return PostCommentStatusEnum.PERMISSION_DENIED;
            }

            // 查找所有需要删除的评论ID（当前评论及其所有子评论）
            List<Long> commentIdsToDelete = findAllCommentIdsToDelete(commentId);

            // 删除所有关联的点赞记录
            deleteAllLikeRecords(commentIdsToDelete);

            // 删除所有评论（包括子评论）
            deleteComments(commentIdsToDelete);

            return PostCommentStatusEnum.SUCCESS;
        } catch (Exception e) {
            return PostCommentStatusEnum.SYSTEM_ERROR;
        }
    }

    /**
     * 查找所有需要删除的评论ID（当前评论及其所有子评论）
     *
     * @param rootCommentId 根评论ID
     * @return 需要删除的评论ID列表
     */
    private List<Long> findAllCommentIdsToDelete(Long rootCommentId) {
        List<Long> commentIdsToDelete = new ArrayList<>();
        commentIdsToDelete.add(rootCommentId); // 首先添加根评论ID

        // 使用BFS遍历所有子评论
        Queue<Long> queue = new LinkedList<>();
        queue.offer(rootCommentId);

        while (!queue.isEmpty()) {
            Long parentId = queue.poll();

            // 查询当前评论的所有子评论
            LambdaQueryWrapper<QuestionComment> queryWrapper = new LambdaQueryWrapper<QuestionComment>()
                    .eq(QuestionComment::getParentId, parentId);
            
            List<QuestionComment> childComments = commentMapper.selectList(queryWrapper);
            
            for (QuestionComment childComment : childComments) {
                Long childId = childComment.getId();
                commentIdsToDelete.add(childId);
                queue.offer(childId); // 添加到队列中继续查找其子评论
            }
        }

        return commentIdsToDelete;
    }

    /**
     * 删除评论相关的所有点赞记录
     *
     * @param commentIds 评论ID列表
     */
    private void deleteAllLikeRecords(List<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return;
        }
        
        LambdaQueryWrapper<QuestionCommentLike> queryWrapper = new LambdaQueryWrapper<QuestionCommentLike>()
                .in(QuestionCommentLike::getCommentId, commentIds);
        
        commentLikeMapper.delete(queryWrapper);
    }

    /**
     * 删除评论
     *
     * @param commentIds 评论ID列表
     */
    private void deleteComments(List<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return;
        }
        
        commentMapper.deleteByIds(commentIds);
    }

    // 根据VirtualId获取原始ID
    public Long getOriginalIdFromVirtualId(String virtualId) {
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
