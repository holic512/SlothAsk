/**
 * File Name: AnswerRecordResponse.java
 * Description: 用户答题记录响应DTO，用于返回答题记录信息。
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 封装用户答题记录的响应数据，包含记录ID、用户答案、提交状态和更新时间。
 */

package org.example.servicequestion.user.answer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerRecordResponse {
    
    /**
     * 答题记录ID
     */
    private Long id;
    
    /**
     * 用户答案
     */
    private String answer;
    
    /**
     * 是否已提交（0-未提交，1-已提交）
     */
    private Integer isSubmitted;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}