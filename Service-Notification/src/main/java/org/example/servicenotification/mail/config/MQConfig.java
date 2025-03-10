/**
 * File Name: MQConfig.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    // 验证码邮件队列
    @Bean
    public Queue emailVerificationQueue() {
        return new Queue(MailMQQueueName.EMAIL_VERIFICATION_QUEUE.getValue(), true, false, false);
    }

    // 将验证码邮件队列与交换机绑定
    @Bean
    public Binding emailVerificationBinding(Queue emailVerificationQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(emailVerificationQueue)
                .to(directExchange)
                .with(MailMQRoutingKey.EMAIL_VERIFICATION_ROUTING_KEY.getKey());
    }
}
