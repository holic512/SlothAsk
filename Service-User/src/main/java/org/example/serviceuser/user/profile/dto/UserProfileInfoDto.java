/**
 * File Name: UserProfileInfoDto.java
 * Description: 用户个人资料信息数据传输对象，用于封装用户详细信息
 * Author: holic512
 * Created Date: 2025-06-12
 * Version: 1.0
 * Usage:
 * 在用户个人资料页面展示用户的详细信息，包括基本资料、社交状态和编程经验等，支持判断访问者身份
 */
package org.example.serviceuser.user.profile.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户信息数据传输对象
 */
@Data
public class UserProfileInfoDto {
    // profile 表中的

    /**
     * 用户头像URL 需要额外处理
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 加入日期
     */
    private LocalDate joinDate;

    /**
     * 个人简介/签名
     */
    private String bio;

    /**
     * 性别（示例："男"/"女"/"未知"）
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 所在省份（示例："江苏省"）
     */
    private String province;


    //  逻辑处理获得的

    /**
     * 是否为用户本人访问
     */
    private Boolean isSelf;


    /**
     * 编程年龄（示例："3年"）
     */
    private String codingAge;


    // 关注表的

    /**
     * 关注人数
     */
    private Integer followingCount;

    /**
     * 粉丝数量
     */
    private Integer followersCount;


    /**
     * 当前用户是否已关注该用户
     */
    private Boolean isFollowed;


}