/**
 * File Name: UserAnswerAiAnalysis.java
 * Description: 用户答案AI分析实体类
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 */
package org.example.servicequestion.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_answer_ai_analysis")
public class UserAnswerAiAnalysis {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("record_id")
    private Long recordId;

    @TableField("accuracy_rate")
    private Double accuracyRate;

    @TableField("ai_explanation")
    private String aiExplanation;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
