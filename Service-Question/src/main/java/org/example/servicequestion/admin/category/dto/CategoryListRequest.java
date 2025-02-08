package org.example.servicequestion.admin.category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分类列表请求参数
 */
@Data
public class CategoryListRequest {
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 项目ID
     */
    private Long projectId;
    
    /**
     * 状态(1:正常 0:禁用)
     */
    private Integer status;
    
    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum;
    
    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小为1")
    private Integer pageSize;
} 