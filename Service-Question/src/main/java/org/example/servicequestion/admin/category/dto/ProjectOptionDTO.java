package org.example.servicequestion.admin.category.dto;

import lombok.Data;

/**
 * 项目选项DTO
 */
@Data
public class ProjectOptionDTO {
    
    /**
     * 项目ID
     */
    private Long id;
    
    /**
     * 项目名称
     */
    private String name;
    
    /**
     * 项目描述
     */
    private String description;
} 