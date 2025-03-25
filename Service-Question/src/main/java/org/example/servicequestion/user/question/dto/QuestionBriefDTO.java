package org.example.servicequestion.user.question.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 简化版题目DTO，用于列表展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBriefDTO {
    
    /**
     * 题目ID
     */
    private Long id;
    
    /**
     * 虚拟ID
     */
    private String virtualId;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 题目类型
     */
    private Integer type;
    
    /**
     * 难度级别
     */
    private Integer difficulty;
    
    /**
     * 是否为当前正在查看的题目
     */
    private boolean isCurrent;
} 