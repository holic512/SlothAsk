/**
 * File Name: QuestionComment.java
 * Description: 题目评论实体类
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.serviceuser.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("question_comment")
public class QuestionComment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("question_id")
    private Long questionId;

    @TableField("user_id")
    private Long userId;

    @TableField("content")
    private String content;

    @TableField("parent_id")
    private Long parentId;

    @TableField("like_count")
    private Integer likeCount = 0;

    @TableField("status")
    private Integer status = 1;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否当前用户已点赞（前端展示用）
     */
    @TableField(exist = false)
    private Boolean likedByCurrentUser;
}
