package org.example.servicequestion.user.question.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 题目列表DTO，包含题目列表和分页信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListDTO {
    
    /**
     * 当前题目在分类中的索引位置（从1开始）
     */
    private int currentIndex;
    
    /**
     * 分类中的总题目数
     */
    private int totalCount;
    
    /**
     * 当前页码
     */
    private int currentPage;
    
    /**
     * 总页数
     */
    private int totalPages;
    
    /**
     * 每页显示的题目数量
     */
    private int pageSize;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 题目列表（简化版，不包含完整内容和答案）
     */
    private List<QuestionBriefDTO> questions;
} 