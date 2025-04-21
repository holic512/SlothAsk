/**
 * File Name: PostQuestionCommentController.java
 * Description: 问题评论控制器，提供问题评论的增删改查及相关操作接口
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 * Usage:
 * - 添加评论或回复: POST /user/questionComment/add
 * - 点赞评论: POST /user/questionComment/like/{commentId}
 * - 取消点赞: POST /user/questionComment/unlike/{commentId}
 * - 删除评论: POST /user/questionComment/delete/{commentId}
 */
package org.example.servicequestion.user.questionComment.Controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.questionComment.Service.PostQuestionCommentService;
import org.example.servicequestion.user.questionComment.dto.CommentRequestDTO;
import org.example.servicequestion.user.questionComment.enums.PostCommentStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/questionComment")
public class PostQuestionCommentController {

    @Autowired
    private PostQuestionCommentService postQuestionCommentService;

    /**
     * 添加评论或回复
     *
     * @param userId     当前用户ID，从请求头获取
     * @param requestDTO 评论请求
     * @return API响应
     */
    @PostMapping("/add")
    public ApiResponse addComment(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestBody CommentRequestDTO requestDTO) {

        // 调用服务添加评论，返回状态枚举
        PostCommentStatusEnum status = postQuestionCommentService.addComment(userId, requestDTO);

        // 根据状态枚举返回相应的API响应
        return new ApiResponse(status.getCode(), status.getMessage());
    }

    /**
     * 点赞评论
     *
     * @param userId    当前用户ID，从请求头获取
     * @param commentId 评论ID
     * @return API响应
     */
    @PostMapping("/like/{commentId}")
    public ApiResponse likeComment(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable("commentId") Long commentId) {

        // 调用服务点赞评论，返回状态枚举
        PostCommentStatusEnum status = postQuestionCommentService.likeComment(userId, commentId);

        // 根据状态枚举返回相应的API响应
        return new ApiResponse(status.getCode(), status.getMessage());
    }

    /**
     * 取消点赞评论
     *
     * @param userId    当前用户ID，从请求头获取
     * @param commentId 评论ID
     * @return API响应
     */
    @PostMapping("/unlike/{commentId}")
    public ApiResponse unlikeComment(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable("commentId") Long commentId) {

        // 调用服务取消点赞评论，返回状态枚举
        PostCommentStatusEnum status = postQuestionCommentService.unlikeComment(userId, commentId);

        // 根据状态枚举返回相应的API响应
        return new ApiResponse(status.getCode(), status.getMessage());
    }

    /**
     * 删除评论（包括所有子评论）
     *
     * @param userId    当前用户ID，从请求头获取
     * @param commentId 评论ID
     * @return API响应
     */
    @PostMapping("/delete/{commentId}")
    public ApiResponse deleteComment(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable("commentId") Long commentId) {

        // 调用服务删除评论，返回状态枚举
        PostCommentStatusEnum status = postQuestionCommentService.deleteComment(userId, commentId);

        // 根据状态枚举返回相应的API响应
        return new ApiResponse(status.getCode(), status.getMessage());
    }
}
