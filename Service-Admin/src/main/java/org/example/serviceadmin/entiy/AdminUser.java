/**
 * 文件名: AdminUser.java
 * 描述: 表示与数据库表 "admin_user" 对应的管理员用户实体类。
 * 作者: holic512
 * 创建日期: 2025-01-15
 * 版本: 1.0
 * 用途:
 * 本类用于通过 MyBatis-Plus 框架操作 "admin_user" 表。
 */
package org.example.serviceadmin.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员用户实体类
 */
@Data
@TableName("admin_user") // 映射到数据库表 "admin_user"
public class AdminUser {

    /**
     * 主键 ID，自动增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 角色 ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 上次登录 IP 地址
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 上次登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 用户状态（例如: 1-启用, 0-禁用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
