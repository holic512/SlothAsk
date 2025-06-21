/**
 * File Name: PointsRecord.java
 * Description: 积分变动记录实体类，用于记录用户每一次积分的变化情况
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("points_record")
public class PointsRecord {
    // 记录ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户ID
    @TableField("user_id")
    private Long userId;

    // 积分变动值（正数为获得，负数为使用）
    @TableField("points")
    private Integer points;

    // 类型（1: 获得，2: 使用）
    @TableField("type")
    private Integer type;

    // 积分来源（1: 签到，2: 答题，3: 购买题库，4: 其他）
    @TableField("source")
    private Integer source;

    // 变动说明
    @TableField("description")
    private String description;

    // 创建时间
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
