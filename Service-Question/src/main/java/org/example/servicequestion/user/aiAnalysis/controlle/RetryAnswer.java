/**
 * File Name: RetryAnswer.java
 * Description: 重新回答控制器，提供重新回答的接口
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用户通过 POST 请求调用 /user/aiAnalysis/retryAnswer 重新回答，
 * 请求需携带请求头 X-User-Id 和请求体包含 answerId（回答记录ID）。
 * 控制器将请求转发至 RetryAnswerService 并返回统一响应结构。
 */
package org.example.servicequestion.user.aiAnalysis.controlle;

import jakarta.validation.Valid;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.aiAnalysis.enums.RetryAnswerEnum;
import org.example.servicequestion.user.aiAnalysis.request.RetryAnswerRequest;
import org.example.servicequestion.user.aiAnalysis.service.RetryAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/aiAnalysis")
public class RetryAnswer {

    private final RetryAnswerService retryAnswerService;

    @Autowired
    public RetryAnswer(RetryAnswerService retryAnswerService) {
        this.retryAnswerService = retryAnswerService;
    }

    @PostMapping("retryAnswer")
    public ApiResponse retryAnswer(
            @RequestHeader(value = "X-User-Id") Long userId,
            @Valid @RequestBody RetryAnswerRequest request) {
        
        RetryAnswerEnum result = retryAnswerService.retryAnswer(
                request.getAnswerId(), userId);

        return switch (result) {
            case SUCCESS -> ApiResponse.ok("重新回答设置成功");
            case ANSWER_NOT_FOUND, UNAUTHORIZED, AI_PARSING_IN_PROGRESS -> 
                ApiResponse.error(result.getCode(), result.getMessage());
            default -> ApiResponse.error(result.getCode(), "重新回答设置失败");
        };
    }
}