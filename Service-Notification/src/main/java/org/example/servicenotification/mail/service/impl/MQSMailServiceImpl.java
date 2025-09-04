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

import org.example.servicecommon.mail.MailCodeMessage;
import org.example.servicenotification.mail.config.MailMQConfig;
import org.example.servicenotification.mail.service.MQSMailService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSMailServiceImpl implements MQSMailService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MQSMailServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendVerificationEmail(String email, String username, String purpose, String verificationCode) {
        // 构造邮件消息对象
        MailCodeMessage mailCodeMessage = new MailCodeMessage(email, username, purpose, verificationCode);

        // // 序列化为 JSON
        // String message = objectMapper.writeValueAsString(mailCodeMessage);

        // 发送消息到 RabbitMQ 采用 rabbit的 自动序列化
        rabbitTemplate.convertAndSend(
                MailMQConfig.EMAIL_VERIFICATION_EXCHANGE,
                MailMQConfig.EMAIL_VERIFICATION_ROUTING_KEY,
                mailCodeMessage);
    }
}

