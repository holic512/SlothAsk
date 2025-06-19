/**
 * File Name: AiAnalysisMessage.java
 * Description: 用于 AI 解析队列的消息体，仅包含回答 ID。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 */
package org.example.servicequestion.user.aiAnalysis.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class AiAnalysisMessage implements Serializable {

    /**
     * 回答唯一标识（用于后续查找用户答案和标准答案）
     */
    private String answerId;

    public AiAnalysisMessage() {}

    public AiAnalysisMessage(String answerId) {
        this.answerId = answerId;
    }
}