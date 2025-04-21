/**
 * File Name: UserProfile.java
 * Description: 用户个人资料实体类
 * Author: lv
 * Created Date: 2025-03-18
 * Version: 1.2
 * Usage:
 * 用户的扩展信息，包括头像、昵称、性别、生日等
 */
package org.example.servicequestion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @TableField("birthday")
    private LocalDate birthday; // 生日，使用 LocalDate 只存 年-月-日

    @TableField("location")
    private String location; // 所属地（城市/国家）

    @TableField("occupation")
    private Integer occupation; // 对应职业枚举


    @TableField("bio")
    private String bio;

    @TableField("display_achievement_id")
    private Long displayAchievementId;

    @TableField(value = "create_time", exist = false)
    private LocalDateTime createTime;

    @TableField(value = "update_time", exist = false)
    private LocalDateTime updateTime;

    // 便捷构造方法
    public UserProfile(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
