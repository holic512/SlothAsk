/**
 * File Name: AiAnalysisResponse.java
 * Description: AI 解析响应体，包含评分准确率与分析内容
 * Author: holic512
 * Created Date: 2025-06-19
 * Version: 1.0
 */
package org.example.serviceai.userAnswer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResponse {

    /**
     * 回答准确度，范围 0～100
     */
    private int accuracy;

    /**
     * AI 分析内容，指出错误、误解或遗漏，提供专业解释
     */
    private String analysis;
}
