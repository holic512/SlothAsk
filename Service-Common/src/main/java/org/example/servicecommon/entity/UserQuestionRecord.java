/**
 * File Name: UserQuestionRecord.java
 * Description: 用户答题记录实体类
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_question_record")
public class UserQuestionRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("question_id")
    private Long questionId;

    @TableField("user_answer")
    private String userAnswer;

    @TableField("is_submitted")
    private Integer isSubmitted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
