/**
 * File Name: UserProfile.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_profile")
public class UserProfile {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar")
    private String avatar;

    @TableField("gender")
    private Integer gender;

    @TableField("age")
    private Integer age;

    @TableField("bio")
    private String bio;

    @TableField("display_achievement_id")
    private Long displayAchievementId;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
