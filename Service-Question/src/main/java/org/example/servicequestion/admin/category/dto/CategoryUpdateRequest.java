package org.example.servicequestion.admin.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateRequest {
    @NotNull(message = "分类ID不能为空")
    private Long id;
    
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    @NotNull(message = "分类名称不能为空")
    private String name;
    
    private String description;
    
    private String avatarUrl;
    
    private Integer sortOrder;
    
    @NotNull(message = "状态不能为空")
    private Integer status;
} 