/**
 * File Name: Question.java
 * Description: 题库内容实体类
 * Author: lv
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

@Data
@TableName("question")
public class Question {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("category_id")
    private Long categoryId;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("answer")
    private String answer;

    @TableField("difficulty")
    private Integer difficulty;

    @TableField("type")
    private Integer type;

    @TableField("tags")
    private String tags;

    @TableField("status")
    private Integer status;

    @TableField("view_count")
    private Long viewCount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
} 