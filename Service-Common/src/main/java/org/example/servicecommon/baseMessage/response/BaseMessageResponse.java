/**
 * File Name: BaseMessageDto.java
 * Description: 通用消息数据传输对象，用于封装消息的基础信息和内容
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 该类对应前端的 BaseMessage 接口，用于返回给前端的响应体。
 */
package org.example.servicecommon.baseMessage.response;

import lombok.Data;

@Data
public class BaseMessageResponse<T> {

    /**
     * 消息唯一标识
     */
    private String messageId;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 阅读状态，0未读，1已读
     */
    private Integer readStatus;

    /**
     * 消息创建时间，格式化后用于展示
     */
    private String createTime;

    /**
     * 消息内容，根据消息类型不同泛型T具体定义
     */
    private T messageData;
}
