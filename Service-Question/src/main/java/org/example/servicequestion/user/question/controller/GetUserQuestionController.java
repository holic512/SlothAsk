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
import org.example.servicequestion.entity.Question;
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
    @GetMapping("/question/{virtualId}/answer")
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
     * @param pageSize 每页显示的题目数量，默认为10
     * @return 包含题目列表的API响应
     */
    @GetMapping("/category-questions/{virtualId}")
    public ApiResponse getCategoryQuestionsByVirtualId(
            @PathVariable String virtualId,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        QuestionListDTO result = getUserQuestionService.getCategoryQuestionsByVirtualId(virtualId, pageSize);
        
        if (result == null) {
            return new ApiResponse(404, "题目不存在", null);
        }
        
        return new ApiResponse(200, "获取分类题目列表成功", result);
    }
}
