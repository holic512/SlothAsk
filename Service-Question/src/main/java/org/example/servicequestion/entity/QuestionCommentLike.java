/**
 * File Name: QuestionCommentLike.java
 * Description: 题目评论点赞记录实体类
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("question_comment_like")
public class QuestionCommentLike {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("comment_id")
    private Long commentId;

    @TableField("user_id")
    private Long userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
