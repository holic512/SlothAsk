/**
 * File Name: AiBaseMessageDto.java
 * Description: AI识别消息数据传输对象，用于封装AI解析相关的数据
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 该类对应前端的 AiRecognitionMessageData 接口，用于数据传输和交互。
 */
package org.example.servicecommon.baseMessage.dto;

import lombok.Data;

@Data
public class AiBaseMessageDto {
    /**
     * 题目ID
     */
    private String questionId;

    /**
     * 题目标题
     */
    private String questionTitle;

    /**
     * AI解析正确率（如87表示87%）
     */
    private Integer accuracy;
}
