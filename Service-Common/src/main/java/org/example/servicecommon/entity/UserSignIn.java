/**
 * File Name: UserSignIn.java
 * Description: 签到记录实体类，用于存储用户每日签到记录信息
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_sign_in")
public class UserSignIn {
    // 签到ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户ID
    @TableField("user_id")
    private Long userId;

    // 签到日期（仅日期）
    @TableField("sign_date")
    private LocalDate signDate;

    // 具体签到时间（含时分秒）
    @TableField("sign_time")
    private LocalDateTime signTime;

    // 签到IP地址
    @TableField("sign_ip")
    private String signIp;

    // 记录创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 记录更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
