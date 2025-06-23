/**
 * File Name: UserDailySubmitCount.java
 * Description: 用户每日提交次数实体类，用于记录每个用户每天的提交次数
 * Author: holic512
 * Created Date: 2025-06-23
 * Version: 1.0
 */

package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_daily_submit_count")
public class UserDailySubmitCount {
    // 主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户ID
    @TableField("user_id")
    private Long userId;

    // 当天提交次数
    @TableField("submit_count")
    private Integer submitCount;

    // 提交日期（年月日）
    @TableField("submit_date")
    private LocalDate submitDate;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
