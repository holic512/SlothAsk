/**
 * File Name: PostAnswerQuestionController.java
 * Description: 用户答题功能的接口控制器，提供保存答案的接口。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 用户通过 POST 请求调用 /user/answer/saveAnswer 保存答案，
 * 请求需携带请求头 X-User-Id 和请求体包含 virtualId（加密后的题目ID）和 answer（用户答案）。
 * 控制器将请求转发至 PostAnswerQuestionService 并返回统一响应结构。
 */
package org.example.servicequestion.user.answer.controller;

import jakarta.validation.Valid;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.answer.enums.PostAnswerQuestionEnum;
import org.example.servicequestion.user.answer.request.SaveAnswerRequest;
import org.example.servicequestion.user.answer.request.SubmitAnswerRequest;
import org.example.servicequestion.user.answer.service.PostAnswerQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/answer")
public class PostAnswerQuestionController {

    private final PostAnswerQuestionService postAnswerQuestionService;

    @Autowired
    public PostAnswerQuestionController(PostAnswerQuestionService postAnswerQuestionService) {
        this.postAnswerQuestionService = postAnswerQuestionService;
    }

    @PostMapping("saveAnswer")
    public ApiResponse saveAnswer(
            @RequestHeader(value = "X-User-Id") Long userId,
            @Valid @RequestBody SaveAnswerRequest request) {
        
        PostAnswerQuestionEnum result = postAnswerQuestionService.saveAnswer(
                request.getVirtualId(), userId, request.getAnswer());

        return switch (result) {
            case SUCCESS -> ApiResponse.ok("答案保存成功");
            case ALREADY_SUBMITTED, QUESTION_NOT_FOUND -> ApiResponse.error(result.getCode(), result.getMessage());
            default -> ApiResponse.error(result.getCode(), "答案保存失败");
        };
    }

    @PostMapping("submitAnswer")
    public ApiResponse submitAnswer(
            @RequestHeader(value = "X-User-Id") Long userId,
            @Valid @RequestBody SubmitAnswerRequest request) {
        
        PostAnswerQuestionEnum result = postAnswerQuestionService.submitAnswer(
                request.getAnswerId(), userId);

        return switch (result) {
            case SUCCESS -> ApiResponse.ok("答案提交成功");
            case ALREADY_SUBMITTED, ANSWER_NOT_FOUND, UNAUTHORIZED -> ApiResponse.error(result.getCode(), result.getMessage());
            default -> ApiResponse.error(result.getCode(), "答案提交失败");
        };
    }
}
