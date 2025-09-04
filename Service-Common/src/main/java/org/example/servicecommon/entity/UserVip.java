/**
 * File Name: UserVip.java
 * Description: 用户VIP信息实体类
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_vip")
public class UserVip {

    // 主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户ID，外键
    @TableField("user_id")
    private Long userId;

    // VIP类型：1=月付，2=永久
    @TableField("vip_type")
    private Integer vipType;

    // VIP开始时间，仅月付有效
    @TableField("start_time")
    private LocalDateTime startTime;

    // VIP到期时间，仅月付有效
    @TableField("end_time")
    private LocalDateTime endTime;

    // 记录创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 记录更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}