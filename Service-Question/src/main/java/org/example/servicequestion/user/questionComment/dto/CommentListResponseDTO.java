/**
 * File Name: CommentListResponseDTO.java
 * Description: 评论列表响应DTO
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentListResponseDTO {
    
    /**
     * 总评论数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 当前页码
     */
    private Integer currentPage;
    
    /**
     * 评论列表
     */
    private List<CommentDTO> comments;
} 