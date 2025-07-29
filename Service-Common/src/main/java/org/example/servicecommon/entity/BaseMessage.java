/**
 * File Name: BaseMessage.java
 * Description: 用户消息实体类，用于存储系统推送给用户的消息内容
 * Author: holic512
 * Created Date: 2025-06-25
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
@TableName("base_message")
public class BaseMessage {

    // 主键ID
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 接收消息的用户ID
    @TableField("user_id")
    private Long userId;

    // 消息类型（业务中定义的枚举值）
    @TableField("type")
    private Integer type;

    // 阅读状态：0-未读，1-已读
    @TableField("read_status")
    private Integer readStatus;

    // 序列化后的消息内容（JSON字符串）
    @TableField("message_data")
    private String messageData;

    // 记录创建时间（自动填充）
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 记录更新时间（自动填充）
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
