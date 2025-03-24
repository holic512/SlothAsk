/**
 * File Name: QuestionListDto.java
 * Description: 题目列表数据传输对象
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * 用于传输题目列表数据,包含题目的基本信息
 */
package org.example.servicequestion.user.category.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.example.servicequestion.config.MyBatisPlus.JsonListTypeHandler;
import org.example.servicequestion.config.MyBatisPlus.ListTypeHandler;

import java.util.List;
// 弃用
@Data
public class QuestionListDto {
    /** 题目ID */
    private Long id;
    
    /** 题目标题 */
    private String title;
    
    /** 难度等级 */
    private Integer difficulty;
    
    /** 题目类型 */
    @TableField(value = "type")
    private Integer type;

    /** 标签分类ID列表 */
    @TableField(value = "tag_category_id", typeHandler = JacksonTypeHandler.class)
    private List<Integer> tag_category_id;

    /** 浏览量 */
    private Long viewCount;
}
