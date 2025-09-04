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

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailMQConfig {

    public static final String EMAIL_VERIFICATION_QUEUE = "email_verification_queue";
    public static final String EMAIL_VERIFICATION_ROUTING_KEY = "email.verification";
    // 邮件验证交换机名
    public static final String EMAIL_VERIFICATION_EXCHANGE = "mail.exchange";
    @Bean
    public DirectExchange mailExchange() {
        return new DirectExchange(EMAIL_VERIFICATION_EXCHANGE, true, false);
    }

    // 验证码邮件队列
    @Bean
    public Queue emailVerificationQueue() {
        return new Queue(EMAIL_VERIFICATION_QUEUE, true, false, false);
    }

    // 将验证码邮件队列与交换机绑定
    @Bean
    public Binding emailVerificationBinding(Queue emailVerificationQueue, DirectExchange mailExchange) {
        return BindingBuilder.bind(emailVerificationQueue)
                .to(mailExchange)
                .with(EMAIL_VERIFICATION_ROUTING_KEY);
    }
}
