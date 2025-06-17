/**
 * File Name: CommentDto.java
 * Description: 用户评论数据传输对象，用于封装评论相关信息
 * Author: holic512
 * Created Date: 2025-06-16
 * Version: 1.0
 * Usage:
 * 在用户个人资料页面展示用户发表的评论信息，包含关联问题ID、标题、评论内容、点赞数和创建时间
 */
package org.example.serviceuser.user.profile.dto;

import lombok.Data;

@Data
public class CommentDto {
    /**
     * 关联的问题唯一标识
     */
    private String questionTUid;

    /**
     * 问题标题
     */
    private String questionTitle;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer likes;

    /**
     * 创建时间
     */
    private String createdAt;
}
