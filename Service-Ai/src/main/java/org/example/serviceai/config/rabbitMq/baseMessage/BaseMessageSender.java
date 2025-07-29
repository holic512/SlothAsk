/**
 * File Name: BaseMessageSender.java
 * Description: 用户消息发送组件，负责将 BaseMessageMessage 消息发送到 RabbitMQ 队列。
 * 封装了发送逻辑，简化业务层调用。
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 * Usage:
 * 注入该组件后调用 sendMessage(BaseMessageMessage message) 方法即可发送用户消息。
 */
package org.example.serviceai.config.rabbitMq.baseMessage;

import org.example.servicecommon.baseMessage.message.BaseMessageMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public BaseMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送 消息到 用户消息队列(消息提醒等)
     *
     * @param message 待发送的消息 DTO
     */
    public void sendMessage(BaseMessageMessage<?> message) {
        try {
            rabbitTemplate.convertAndSend(
                    BaseMessageMqConfig.MESSAGE_EXCHANGE,
                    BaseMessageMqConfig.MESSAGE_ROUTING_KEY,
                    message
            );
        } catch (Exception e) {
            throw new RuntimeException("发送用户消息失败", e);
        }
    }
}
