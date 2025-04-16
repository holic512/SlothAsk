package org.example.servicequestion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户访问问题历史记录实体类(简化版)
 * 只记录用户ID、问题ID和访问时间
 */
@Data
@TableName("user_question_history")
public class UserQuestionHistory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 题目ID
     */
    @TableField("question_id")
    private Long questionId;
    
    /**
     * 访问时间
     */
    @TableField("visit_time")
    private LocalDateTime visitTime;

} 