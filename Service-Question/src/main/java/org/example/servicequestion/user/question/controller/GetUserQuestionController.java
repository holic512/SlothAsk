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

import jakarta.servlet.http.HttpServletRequest;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.question.dto.QuestionDTO;
import org.example.servicequestion.user.question.dto.QuestionListDTO;
import org.example.servicequestion.user.question.dto.QuestionResponseDTO;
import org.example.servicequestion.user.question.service.GetUserQuestionService;
import org.example.servicequestion.user.question.service.QuestionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/question")
public class GetUserQuestionController {

    private final GetUserQuestionService getUserQuestionService;
    private final QuestionHistoryService questionHistoryService;

    @Autowired
    public GetUserQuestionController(GetUserQuestionService getUserQuestionService,
                                     QuestionHistoryService questionHistoryService) {
        this.getUserQuestionService = getUserQuestionService;
        this.questionHistoryService = questionHistoryService;
    }


    /**
     * 根据虚拟ID获取题目信息(不包含答案)
     *
     * @param virtualId 虚拟题目ID
     * @return 包含题目信息的API响应
     */
    @GetMapping("/question/{virtualId}")
    public ApiResponse getQuestionByVirtualId(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable String virtualId,
            HttpServletRequest request) {

        // 问题信息和真实ID  - 主方法
        QuestionResponseDTO response = getUserQuestionService.getQuestionWithRealId(virtualId);
        if (response == null) {
            return new ApiResponse(404, "题目不存在", null);
        }

        // 根据返回值  获取问题信息和真实ID
        QuestionDTO question = response.getQuestion();
        Long questionId = response.getRealId();

        // 1. 异步记录访问历史（独立服务）
        if (userId > 0 && questionId != null) { // 只记录已登录用户的历史
            questionHistoryService.addHistoryRecord(userId, questionId);
        }

        // 2. 异步记录访问量（独立服务）
        String visitorId = "";
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            visitorId = userId + ":" + userAgent.hashCode();
        }
        getUserQuestionService.recordQuestionView(questionId, visitorId);

        // 返回问题信息给前端(不包含真实ID)
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
     * @param page      请求的页码，默认为1
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

}
