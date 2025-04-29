/**
 * File Name: GetQuestionCommentController.java
 * Description: 获取问题评论控制器
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.questionComment.dto.CommentListResponseDTO;
import org.example.servicequestion.user.questionComment.dto.CommentQueryDTO;
import org.example.servicequestion.user.questionComment.service.GetQuestionCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/questionComment")
public class GetQuestionCommentController {

    @Autowired
    private GetQuestionCommentService getQuestionCommentService;

    /**
     * 获取问题评论列表
     *
     * @param queryDTO 查询参数
     * @return API响应
     */
    @GetMapping("/list")
    public ApiResponse getCommentList(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            CommentQueryDTO queryDTO) {
        if (queryDTO.getTargetVirtualId() == null || queryDTO.getTargetVirtualId().isEmpty()) {
            return new ApiResponse(401, "目标ID不能为空");
        }

        CommentListResponseDTO responseDTO = getQuestionCommentService.getCommentsByVirtualId(queryDTO, userId);
        return new ApiResponse(200, "获取成功", responseDTO);
    }
}
