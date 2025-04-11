/**
 * File Name: GetUserQuestionController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.user.question.controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.question.dto.CommentGetDTO;
import org.example.servicequestion.user.question.dto.CommentPostDTO;
import org.example.servicequestion.user.question.dto.QuestionDTO;
import org.example.servicequestion.user.question.dto.QuestionListDTO;
import org.example.servicequestion.user.question.service.GetUserQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/question")
public class GetUserQuestionController {

    private final GetUserQuestionService getUserQuestionService;

    @Autowired
    public GetUserQuestionController(GetUserQuestionService getUserQuestionService) {
        this.getUserQuestionService = getUserQuestionService;
    }


    /**
     * 根据虚拟ID获取题目信息(不包含答案)
     *
     * @param virtualId 虚拟题目ID
     * @return 包含题目信息的API响应
     */
    @GetMapping("/question/{virtualId}")
    public ApiResponse getQuestionByVirtualId(
            @PathVariable String virtualId) {
        QuestionDTO question = getUserQuestionService.getQuestionByVirtualId(virtualId);
        if (question == null) {
            return new ApiResponse(404, "题目不存在", null);
        }
        return new ApiResponse(200, "获取题目信息成功", question);
    }

    /**
     * 根据虚拟ID获取题目答案
     *
     * @param virtualId 虚拟题目ID
     * @return 包含题目答案的API响应
     */
    @GetMapping("/question/answer/{virtualId}")
    public ApiResponse getQuestionAnswerByVirtualId(
            @PathVariable String virtualId) {
        String answer = getUserQuestionService.getQuestionAnswerByVirtualId(virtualId);
        if (answer == null) {
            return new ApiResponse(404, "题目答案不存在", null);
        }
        return new ApiResponse(200, "获取题目答案成功", answer);
    }
    
    /**
     * 根据虚拟ID获取同分类下的题目列表，并确保当前题目在返回的列表中
     *
     * @param virtualId 虚拟题目ID
     * @param page 请求的页码，默认为1
     * @return 包含题目列表的API响应
     */
    @GetMapping("/category-questions/{virtualId}")
    public ApiResponse getCategoryQuestionsByVirtualId(
            @PathVariable String virtualId,
            @RequestParam(defaultValue = "1") int page) {
        
        QuestionListDTO result = getUserQuestionService.getCategoryQuestionsByVirtualId(virtualId, page);
        
        if (result == null) {
            return new ApiResponse(404, "题目不存在", null);
        }
        
        return new ApiResponse(200, "获取分类题目列表成功", result);
    }

    /**
     * 根据虚拟ID获取评论列表
     *
     * @param virtualId 虚拟题目ID
     * @return 包含评论列表的API响应
     */
    @GetMapping("/comment/{virtualId}")
    public ApiResponse getCommentsByVirtualId(
            @PathVariable String virtualId) {
        List<CommentGetDTO> comments = getUserQuestionService.getCommentsByVirtualId(virtualId);
        if (comments == null) {
            return new ApiResponse(404, "评论不存在", null);
        }
        return new ApiResponse(200, "获取评论列表成功", comments);
    }

    @PostMapping("/comment/add")
    public ApiResponse addComment(@RequestBody CommentPostDTO dto) {
        System.out.println("接收到评论请求：" + dto.getQuestionId() + " " + dto.getUserId() + " " + dto.getContent());
        try {
            getUserQuestionService.addComment(dto.getQuestionId(), dto.getUserId(), dto.getContent(), dto.getParentId());
            return new ApiResponse(200, "添加评论成功", null);
        } catch (Exception e) {
            return new ApiResponse(500, "添加评论失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/comment/like")
    public ApiResponse updateLikeCount(
            @RequestParam Long commentId) {
        System.out.println("点赞ID：" + commentId);
        getUserQuestionService.updateLikeCount(commentId);
        return new ApiResponse(200, "更新点赞数成功", null);
    }
}
