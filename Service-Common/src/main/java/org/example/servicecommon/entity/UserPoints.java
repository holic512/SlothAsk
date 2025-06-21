/**
 * File Name: UserPoints.java
 * Description: 用户积分实体类，用于记录用户当前积分、累计积分和已用积分等情况
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_points")
public class UserPoints {
    // 主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户ID
    @TableField("user_id")
    private Long userId;

    // 当前可用积分
    @TableField("points")
    private Integer points;

    // 累计获得积分
    @TableField("total_points")
    private Integer totalPoints;

    // 已使用积分
    @TableField("used_points")
    private Integer usedPoints;

    // 创建时间
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
