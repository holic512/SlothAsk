/**
 * File Name: AiAnalysisRequest.java
 * Description: AI 解析请求体，包含题目、标准答案与学生答案
 * Author: holic512
 * Created Date: 2025-06-19
 * Version: 1.0
 */
package org.example.serviceai.userAnswer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiAnalysisRequest {

    /**
     * 题目内容
     */
    @NotBlank(message = "题目内容不能为空")
    private String question;

    /**
     * 标准正确答案
     */
    @NotBlank(message = "标准答案不能为空")
    private String correctAnswer;

    /**
     * 学生作答内容，纯文本
     */
    @NotBlank(message = "学生作答不能为空")
    private String studentAnswer;
}
