/**
 * File Name: User.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * 用于获取用户列表
 */
package org.example.serviceuser.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    private Integer gender;
    private Integer age;
    private String avatar;
    private String bio;
    private Date createTime;

    private String password;

}
