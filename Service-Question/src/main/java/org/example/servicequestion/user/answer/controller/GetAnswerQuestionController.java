/**
 * File Name: GetAnswerQuestionController.java
 * Description: 获取用户答题记录功能的接口控制器，提供查询答题记录的接口。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 用户通过 GET 请求调用 /user/answer/getAnswerRecord 查询答题记录，
 * 请求需携带请求头 X-User-Id 和参数 virtualId（加密后的题目ID）。
 * 控制器将请求转发至 GetAnswerQuestionService 并返回统一响应结构。
 */
package org.example.servicequestion.user.answer.controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.answer.dto.AnswerRecordResponse;
import org.example.servicequestion.user.answer.service.GetAnswerQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/answer")
public class GetAnswerQuestionController {

    private final GetAnswerQuestionService getAnswerQuestionService;

    @Autowired
    public GetAnswerQuestionController(GetAnswerQuestionService getAnswerQuestionService) {
        this.getAnswerQuestionService = getAnswerQuestionService;
    }

    @GetMapping("getAnswerRecord")
    public ApiResponse getAnswerRecord(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam String virtualId) {
        
        AnswerRecordResponse response = getAnswerQuestionService.getAnswerRecord(virtualId, userId);
        
        if (response != null) {
            return ApiResponse.ok("查询成功", response);
        } else {
            return ApiResponse.error(404, "答题记录不存在");
        }
    }
}
