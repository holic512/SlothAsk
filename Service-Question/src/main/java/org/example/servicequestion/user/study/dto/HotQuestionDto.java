/**
 * File Name: HotQuestionDto.java
 * Description: 热门题目数据传输对象
 * Author: holic512
 * Created Date: 2025-01-20
 * Version: 1.0
 * Usage:
 * 用于传输热门题目的基本信息，包括虚拟ID、标题和浏览量
 */
package org.example.servicequestion.user.study.dto;

import lombok.Data;

/**
 * 热门题目数据传输对象
 * 包含题目的基本信息用于前端展示
 */
@Data
public class HotQuestionDto {
    
    /**
     * 题目虚拟ID
     */
    private String virtualId;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 浏览量
     */
    private Long viewCount;
    
    /**
     * 构造函数
     */
    public HotQuestionDto() {}
    
    /**
     * 带参构造函数
     * 
     * @param virtualId 虚拟ID
     * @param title 标题
     * @param viewCount 浏览量
     */
    public HotQuestionDto(String virtualId, String title, Long viewCount) {
        this.virtualId = virtualId;
        this.title = title;
        this.viewCount = viewCount;
    }
}