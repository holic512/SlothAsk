/**
 * File Name: GetAiAnalysis.java
 * Description: 获取AI解析记录控制器，提供查询AI解析记录的接口
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用户通过 POST 请求调用 /user/aiAnalysis/getRecord 获取AI解析记录，
 * 请求需携带请求头 X-User-Id 和请求体包含 answerId（回答记录ID）。
 * 控制器将请求转发至 GetAiAnalysisService 并返回统一响应结构。
 */
package org.example.servicequestion.user.aiAnalysis.controlle;

import jakarta.validation.Valid;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.aiAnalysis.request.GetAiAnalysisRequest;
import org.example.servicequestion.user.aiAnalysis.service.GetAiAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/aiAnalysis")
public class GetAiAnalysis {

    private final GetAiAnalysisService getAiAnalysisService;

    @Autowired
    public GetAiAnalysis(GetAiAnalysisService getAiAnalysisService) {
        this.getAiAnalysisService = getAiAnalysisService;
    }

    @PostMapping("getAiAnalysis")
    public ApiResponse getAiAnalysisRecord(
            @RequestHeader(value = "X-User-Id") Long userId,
            @Valid @RequestBody GetAiAnalysisRequest request) {
        
        GetAiAnalysisService.GetAiAnalysisResult result = getAiAnalysisService.getAiAnalysisRecord(
                request.getAnswerId(), userId);

        return switch (result.getStatus()) {
            case SUCCESS -> ApiResponse.ok("获取AI解析记录成功", result.getData());
            case PARSING_IN_PROGRESS -> ApiResponse.error(result.getStatus().getCode(), result.getStatus().getMessage());
            case ANSWER_NOT_FOUND, UNAUTHORIZED, NO_ANALYSIS_RECORD -> 
                ApiResponse.error(result.getStatus().getCode(), result.getStatus().getMessage());
            default -> ApiResponse.error(result.getStatus().getCode(), "获取AI解析记录失败");
        };
    }
}
