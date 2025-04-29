/**
 * File Name: PostQuestionCommentService.java
 * Description: 问题评论服务接口，定义评论相关业务操作
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 * Usage:
 * - 添加评论：addComment(Long userId, CommentRequestDTO requestDTO)
 * - 点赞评论：likeComment(Long userId, Long commentId)
 * - 取消点赞：unlikeComment(Long userId, Long commentId)
 * - 删除评论：deleteComment(Long userId, Long commentId)
 * 所有方法返回PostCommentStatusEnum枚举，表示操作结果状态
 */
package org.example.servicequestion.user.questionComment.service;

import org.example.servicequestion.user.questionComment.dto.CommentRequestDTO;
import org.example.servicequestion.user.questionComment.enums.PostCommentStatusEnum;

public interface PostQuestionCommentService {

    /**
     * 添加评论
     *
     * @param userId     当前用户ID
     * @param requestDTO 评论请求数据
     * @return 评论状态枚举
     */
    PostCommentStatusEnum addComment(Long userId, CommentRequestDTO requestDTO);

    /**
     * 点赞评论
     *
     * @param userId    当前用户ID
     * @param commentId 评论ID
     * @return 操作状态枚举
     */
    PostCommentStatusEnum likeComment(Long userId, Long commentId);

    /**
     * 取消点赞评论
     *
     * @param userId    当前用户ID
     * @param commentId 评论ID
     * @return 操作状态枚举
     */
    PostCommentStatusEnum unlikeComment(Long userId, Long commentId);
    
    /**
     * 删除评论（包括子评论）
     *
     * @param userId    当前用户ID
     * @param commentId 评论ID
     * @return 操作状态枚举
     */
    PostCommentStatusEnum deleteComment(Long userId, Long commentId);
}
