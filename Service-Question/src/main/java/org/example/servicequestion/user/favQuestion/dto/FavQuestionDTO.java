/**
 * File Name: FavQuestionDTO.java
 * Description: 收藏题目数据传输对象
 * Author: holic512
 * Created Date: 2025-04-29
 * Version: 1.0
 */
package org.example.servicequestion.user.favQuestion.dto;

import java.time.LocalDateTime;

/**
 * 收藏题目DTO
 */
public class FavQuestionDTO {

    /**
     * 题目ID
     */
    private Long questionId;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 收藏时间
     */
    private LocalDateTime createTime;
    
    /**
     * 虚拟ID，由真实ID加密生成
     */
    private String virtualId;

    public FavQuestionDTO() {
    }

    public FavQuestionDTO(Long questionId, String title, LocalDateTime createTime) {
        this.questionId = questionId;
        this.title = title;
        this.createTime = createTime;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getVirtualId() {
        return virtualId;
    }

    public void setVirtualId(String virtualId) {
        this.virtualId = virtualId;
    }
} 