/**
 * File Name: SendAiAnalysisRequest.java
 * Description: 发送AI解析请求的数据传输对象
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用于接收前端发送AI解析请求时传递的请求参数，包含回答记录ID。
 */
package org.example.servicequestion.user.aiAnalysis.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SendAiAnalysisRequest {
    
    /**
     * 回答记录ID
     */
    @NotNull(message = "回答ID不能为空")
    @Positive(message = "回答ID必须为正数")
    private Long answerId;
}