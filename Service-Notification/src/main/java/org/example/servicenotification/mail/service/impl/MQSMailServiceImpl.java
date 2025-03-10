/**
 * File Name: SentMailServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.service.impl;

import org.example.servicenotification.mail.config.MailMQQueueName;
import org.example.servicenotification.mail.config.MailMQRoutingKey;
import org.example.servicenotification.mail.dto.MailCodeMessage;
import org.example.servicenotification.mail.service.MQSMailService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MQSMailServiceImpl implements MQSMailService {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MQSMailServiceImpl(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendVerificationEmail(String email, String username, String purpose, String verificationCode) {
        // 构造邮件消息对象
        MailCodeMessage mailCodeMessage = new MailCodeMessage(email, username, purpose, verificationCode);

        try {
            // 序列化为 JSON
            String message = objectMapper.writeValueAsString(mailCodeMessage);

            // 发送消息到 RabbitMQ
            rabbitTemplate.convertAndSend(
                    "direct.exchange",
                    MailMQRoutingKey.EMAIL_VERIFICATION_ROUTING_KEY.getKey(),
                    message);
        } catch (JsonProcessingException e) {
            System.err.println("邮件消息序列化失败：" + e.getMessage());
        }
    }
}

