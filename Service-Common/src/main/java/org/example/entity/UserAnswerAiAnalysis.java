/**
 * File Name: UserAnswerAiAnalysis.java
 * Description: 用户答案AI分析实体类，用于存储AI对用户答题情况的分析结果
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.2
 */
package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_answer_ai_analysis")
public class UserAnswerAiAnalysis {
    // 主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 关联的答题记录ID
    @TableField("record_id")
    private Long recordId;

    // 答题准确率(0~100的整数)
    @TableField("accuracy_rate")
    private Integer accuracyRate;

    // AI对答题情况的分析解释
    @TableField("ai_explanation")
    private String aiExplanation;

    // AI分析来源(标识使用哪种AI服务进行的分析)
    @TableField("ai_source")
    private String aiSource;

    // 问题内容
    @TableField("question_content")
    private String questionContent;

    // 正确答案
    @TableField("question_answer")
    private String questionAnswer;

    // 用户答案
    @TableField("user_answer")
    private String userAnswer;

    // 逻辑删除标志(0-未删除，1-已删除)
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    // 记录创建时间(自动填充)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 记录更新时间(自动填充)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
