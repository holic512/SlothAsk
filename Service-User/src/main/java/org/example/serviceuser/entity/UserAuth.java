/**
 * File Name: UserAuth.java
 * Description: 第三方用户认证信息实体
 * Author: lv
 * Created Date: 2025-05-07
 * Version: 1.0
 * Usage:
 * 用于存储用户第三方登录平台的绑定信息
 */
package org.example.serviceuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_auth")
public class UserAuth {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("provider")
    private String provider; // 第三方平台，如 github、wechat

    @TableField("open_id")
    private String openId;

    @TableField("union_id")
    private String unionId;

    @TableField("status")
    private Integer status; // 1=正常，0=禁用

    @TableField("is_deleted")
    private Integer isDeleted; // 0=未删除，1=已删除

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}

