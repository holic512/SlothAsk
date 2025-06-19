/**
 * File Name: SendAiAnalysis.java
 * Description: AI解析请求控制器，提供发送AI解析请求的接口
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用户通过 POST 请求调用 /user/aiAnalysis/sendRequest 发送AI解析请求，
 * 请求需携带请求头 X-User-Id 和请求体包含 answerId（回答记录ID）。
 * 控制器将请求转发至 SendAiAnalysisService 并返回统一响应结构。
 */
package org.example.servicequestion.user.aiAnalysis.controlle;

import jakarta.validation.Valid;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.aiAnalysis.enums.SendAiAnalysisEnum;
import org.example.servicequestion.user.aiAnalysis.request.SendAiAnalysisRequest;
import org.example.servicequestion.user.aiAnalysis.service.SendAiAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/aiAnalysis")
public class SendAiAnalysis {

    private final SendAiAnalysisService sendAiAnalysisService;

    @Autowired
    public SendAiAnalysis(SendAiAnalysisService sendAiAnalysisService) {
        this.sendAiAnalysisService = sendAiAnalysisService;
    }

    @PostMapping("sendAiAnalysis")
    public ApiResponse sendAiAnalysisRequest(
            @RequestHeader(value = "X-User-Id") Long userId,
            @Valid @RequestBody SendAiAnalysisRequest request) {
        
        SendAiAnalysisEnum result = sendAiAnalysisService.sendAiAnalysisRequest(
                request.getAnswerId(), userId);

        return switch (result) {
            case SUCCESS -> ApiResponse.ok("AI解析请求发送成功");
            case ANSWER_NOT_FOUND, UNAUTHORIZED, ALREADY_PARSING, NOT_SUBMITTED, ALREADY_ANALYZED -> 
                ApiResponse.error(result.getCode(), result.getMessage());
            default -> ApiResponse.error(result.getCode(), "AI解析请求发送失败");
        };
    }
}
