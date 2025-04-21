/**
 * File Name: CommentDTO.java
 * Description: 评论DTO
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    
    /**
     * 评论ID
     */
    private Long id;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 父评论ID
     */
    private Long parentId;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 作者信息
     */
    private UserInfoDTO userInfo;

    /**
     * 是否点赞 0-未点赞 1-点赞
     */
    private int isLike;

    /**
     * 是否作者 0-不是 1-是
     */
    private int isAuthor;

} 