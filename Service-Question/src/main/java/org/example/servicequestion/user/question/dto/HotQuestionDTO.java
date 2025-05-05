/**
 * File Name: HotQuestionDTO.java
 * Description: 热门题目数据传输对象
 * Author: Claude
 * Created Date: 2025-05-01
 * Version: 1.0
 * Usage:
 * 用于传输热门题目的简要信息，包含虚拟ID、标题和访问量
 */
package org.example.servicequestion.user.question.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门题目DTO，用于热门题目列表展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotQuestionDTO {
    
    /**
     * 虚拟ID
     */
    private String virtualId;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 访问量
     */
    private Long viewCount;
} 