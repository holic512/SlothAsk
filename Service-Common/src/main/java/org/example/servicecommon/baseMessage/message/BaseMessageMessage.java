/**
 * File Name: BaseMessageMessage.java
 * Description: 用于 RabbitMQ 消息传输的用户消息DTO，去除数据库主键及时间字段
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 该类为 BaseMessage 实体的简化版本，用于消息队列传输，不包含数据库字段
 */
package org.example.servicecommon.baseMessage.message;

import lombok.Data;

@Data
public class BaseMessageMessage<T> {

    /**
     * 接收消息的用户ID
     */
    private Long userId;

    /**
     * 消息类型（业务中定义的枚举值）
     */
    private Integer type;

    /**
     * 阅读状态：0-未读，1-已读
     */
    private Integer readStatus;

    /**
     * 序列化后的消息内容（JSON字符串）
     */
    private T messageData;

}
