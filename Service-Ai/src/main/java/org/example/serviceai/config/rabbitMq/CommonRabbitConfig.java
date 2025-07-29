/**
 * File Name: CommonRabbitConfig.java
 * Description: 提供 RabbitMQ 的通用配置，包括消息转换器和 RabbitTemplate 设置，
 *              便于其他模块统一使用 JSON 格式进行消息通信，并支持消息确认机制。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 在 Spring Boot 项目中通过 @Import(CommonRabbitConfig.class) 或自动扫描方式启用，
 * 以统一配置 RabbitMQ 消息的序列化方式和生产者模板，适用于大多数简单的消息发送场景。
 */

package org.example.serviceai.config.rabbitMq;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 通用配置：消息转换器、模板、监听容器
 */
@Configuration
public class CommonRabbitConfig {

    /**
     * JSON 消息转换器
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置并返回一个定制化的 RabbitTemplate 实例用于消息生产。
     *
     * <p>该模板配置了以下特性：
     * <ul>
     *   <li>自定义消息转换器 - 用于消息的序列化/反序列化</li>
     *   <li>强制标志(mandatory) - 确保无法路由的消息能被正确处理</li>
     *   <li>发布确认回调 - 提供消息到达Broker的确认机制</li>
     * </ul>
     *
     * <p><b>注意：</b>要使确认回调生效，需要在Broker端启用 publisher-confirms 功能。
     *
     * @param cf RabbitMQ连接工厂，由Spring自动注入
     * @return 配置好的RabbitTemplate实例
     * @see org.springframework.amqp.rabbit.core.RabbitTemplate
     * @see org.springframework.amqp.support.converter.MessageConverter
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(messageConverter);
        template.setMandatory(true);
        
        // 设置发布确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送成功");
            } else {
                System.err.println("消息发送失败: " + cause);
            }
        });
        
        return template;
    }

    /**
     * 配置消息监听容器工厂，用于消费者端的消息反序列化
     *
     * @param connectionFactory RabbitMQ连接工厂
     * @param messageConverter JSON消息转换器
     * @return 配置好的监听容器工厂
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}