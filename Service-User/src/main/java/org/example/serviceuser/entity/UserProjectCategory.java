/**
 * File Name: UserProjectCategory.java
 * Description: 用户项目分类实体类，记录用户选择的唯一项目分类
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 *  用户项目分类表实体类，与 user_project_category 表对应
 */
package org.example.serviceuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_project_category")
public class UserProjectCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId; // 用户ID，外键关联 user 表

    @TableField("project_id")
    private Long projectId; // 项目分类ID，外键关联 project_category 表

    @TableField("uptime")
    private LocalDateTime uptime; // 更新时间，记录最后修改时间

    @TableField("create_time")
    private LocalDateTime createTime; // 创建时间，记录首次插入时间
}
