/**
 * File Name: SubmitAnswerRequest.java
 * Description: 提交答案请求的数据传输对象
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 用于接收前端提交答案时传递的请求参数，包含回答记录ID。
 */
package org.example.servicequestion.user.answer.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SubmitAnswerRequest {
    
    /**
     * 回答记录ID
     */
    @NotNull(message = "回答ID不能为空")
    @Positive(message = "回答ID必须为正数")
    private Long answerId;
}