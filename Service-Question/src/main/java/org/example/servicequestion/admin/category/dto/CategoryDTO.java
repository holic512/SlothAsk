package org.example.servicequestion.admin.category.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类信息DTO
 */
@Data
public class CategoryDTO {
    
    /**
     * 分类ID
     */
    private Long id;
    
    /**
     * 所属项目ID
     */
    private Long projectId;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 分类描述
     */
    private String description;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    /**
     * 分类头像URL
     */
    private String avatarUrl;
    
    /**
     * 排序序号
     */
    private Integer sortOrder;
    
    /**
     * 访问数量
     */
    private Long viewCount;
    
    /**
     * 状态(1:正常 0:禁用)
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 