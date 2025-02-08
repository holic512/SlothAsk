package org.example.servicequestion.admin.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加分类请求参数
 */
@Data
public class CategoryAddRequest {
    
    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    /**
     * 分类描述
     */
    private String description;

    /**
     * 状态(1:正常 0:禁用)
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    /**
     * 排序号
     */
    private Integer sortOrder = 0;
} 