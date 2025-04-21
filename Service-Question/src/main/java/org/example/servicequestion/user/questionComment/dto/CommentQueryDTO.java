/**
 * File Name: CommentQueryDTO.java
 * Description: 评论查询参数DTO
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.dto;

import lombok.Data;

@Data
public class CommentQueryDTO {
    
    /**
     * 目标虚拟ID
     */
    private String targetVirtualId;
    
    /**
     * 页码，从1开始
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
    
    /**
     * 排序字段: 0-按时间排序(默认), 1-按点赞数排序
     */
    private Integer orderBy = 0;
    
    /**
     * 排序方向: 0-降序(默认), 1-升序
     */
    private Integer orderDirection = 0;
} 