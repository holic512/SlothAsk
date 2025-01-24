/**
 * File Name: UserProfile.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_profile")
public class UserProfileDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer gender; // 1:男, 2:女, 0:未知
    private Integer age;
    private String bio;
    private Long displayAchievementId;
    private Date createTime;
    private Date updateTime;
}
