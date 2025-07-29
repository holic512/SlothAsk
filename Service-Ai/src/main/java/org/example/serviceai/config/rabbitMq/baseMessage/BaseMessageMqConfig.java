package org.example.serviceai.config.rabbitMq.baseMessage; /**
 * File Name: BaseMessageMqConfig.java
 * Description: 用户消息模块的 RabbitMQ 配置类，声明 Direct 交换机、队列及绑定关系
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 */

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseMessageMqConfig {

    // 用户消息队列名
    public static final String MESSAGE_QUEUE = "base.message.queue";

    // 用户消息交换机名
    public static final String MESSAGE_EXCHANGE = "base.message.exchange";

    // 用户消息路由键
    public static final String MESSAGE_ROUTING_KEY = "base.message.route";

    /**
     * 声明 Direct 类型交换机（持久化，非自动删除）
     */
    @Bean
    public DirectExchange messageExchange() {
        return new DirectExchange(MESSAGE_EXCHANGE, true, false);
    }

    /**
     * 声明持久化消息队列
     */
    @Bean
    public Queue messageQueue() {
        return new Queue(MESSAGE_QUEUE, true);
    }

    /**
     * 绑定队列到交换机，并指定路由键
     */
    @Bean
    public Binding messageBinding(Queue messageQueue, DirectExchange messageExchange) {
        return BindingBuilder.bind(messageQueue).to(messageExchange).with(MESSAGE_ROUTING_KEY);
    }


}
