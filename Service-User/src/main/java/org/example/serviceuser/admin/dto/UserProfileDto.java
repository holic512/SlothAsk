/**
 * File Name: UserProfile.java
 * Description: 用户个人资料数据传输对象
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage: 用于在不同层之间传递用户的详细个人资料信息，包括昵称、头像、性别等扩展信息
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
