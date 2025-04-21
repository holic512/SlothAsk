package org.example.servicequestion.user.questionComment.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    // 目标的虚拟ID（问题或回答的虚拟ID）
    private String targetVirtualId;
    
    // 评论内容
    private String content;
    
    // 父评论ID，如果是直接评论则为null
    private Long parentId;
} 