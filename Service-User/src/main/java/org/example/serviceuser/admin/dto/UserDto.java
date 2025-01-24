/**
 * File Name: User.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String password; //可选，不设置则系统生成随机密码
    private Integer status;
    private Integer gender;
    private Integer age;
    private String avatar;
    private String bio;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
