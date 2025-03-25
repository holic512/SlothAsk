/**
 * File Name: Question.java
 * Description: 题库内容实体类
 * Author: holic512
 * Created Date: 2024-03-21
 * Version: 1.0
 */
package org.example.servicequestion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.util.List;

@Data
@TableName(value = "question", autoResultMap = true) // 启用自动结果映射
public class Question {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @TableField("project_id")
    private Long projectId;

    @TableField("category_id")
    private Long categoryId;

    @TableField(value = "title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("answer")
    private String answer;

    @TableField("difficulty")
    private Integer difficulty = 1;

    @TableField("type")
    private Integer type = 1;

    // JSON 字段需要类型处理器
    @TableField(value = "tag_category_ids", typeHandler = JacksonTypeHandler.class)
    private List<Integer> tagCategoryIds;

    @TableField("status")
    private Integer status = 1;

    @TableField("view_count")
    private Long viewCount = 0L;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    /**
     * 虚拟ID，不存在于数据库中
     */
    @TableField(exist = false)
    private String virtualId;
}