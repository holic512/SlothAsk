/**
 * File Name: User.java
 * Description: 用户实体类，定义了系统中用户的基本属性和数据结构
 * Author: holic512
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * 用于用户信息的存储、查询和管理，包含用户ID、用户名、密码、邮箱、电话、状态和时间戳等基本信息
 */
package org.example.servicequestion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
