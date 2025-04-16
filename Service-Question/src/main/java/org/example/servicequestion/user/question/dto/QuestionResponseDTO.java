package org.example.servicequestion.user.question.dto;

import java.io.Serializable;

/**
 * 问题响应DTO，包含问题信息和真实ID
 * 用于内部服务间传递，不直接暴露给前端
 */
public class QuestionResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 问题信息
    private QuestionDTO question;
    
    // 问题的真实ID，仅用于内部服务间通信
    private Long realId;
    
    public QuestionResponseDTO() {
    }
    
    public QuestionResponseDTO(QuestionDTO question, Long realId) {
        this.question = question;
        this.realId = realId;
    }
    
    public QuestionDTO getQuestion() {
        return question;
    }
    
    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }
    
    public Long getRealId() {
        return realId;
    }
    
    public void setRealId(Long realId) {
        this.realId = realId;
    }
} 