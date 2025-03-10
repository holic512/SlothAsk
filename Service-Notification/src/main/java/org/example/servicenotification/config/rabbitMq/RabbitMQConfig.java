/**
 * File Name: RabbitMQConfig.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-10
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.config.rabbitMq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration  // 标记该类为 Spring 的配置类，会在启动时加载该配置
public class RabbitMQConfig {

    // 声明 RabbitMQ 的连接工厂，用于创建与 RabbitMQ 的连接
    private final ConnectionFactory rabbitConnectionFactory;
    // 注入 RabbitMQ 相关属性配置类
    private final RabbitProperties properties;

    // 构造器注入方式，将 ConnectionFactory 和 RabbitProperties 注入到配置类中
    @Autowired
    RabbitMQConfig(ConnectionFactory connectionFactory, RabbitProperties properties) {
        this.rabbitConnectionFactory = connectionFactory;
        this.properties = properties;
    }

    // 定义一个 Direct 类型的交换机，名称为 "direct.exchange"
    // Direct 交换机会按照精确匹配 routingKey 的方式将消息分发到队列
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    // 定义一个 Topic 类型的交换机，名称为 "topic.exchange"
    // Topic 交换机支持通配符匹配 routingKey，可实现模糊匹配规则
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    // 定义一个 Fanout 类型的交换机，名称为 "fanout.exchange"
    // Fanout 交换机会将消息广播到所有与其绑定的队列，忽略 routingKey
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout.exchange");
    }

    // 邮箱监控专用工厂
    @Bean
    public SimpleRabbitListenerContainerFactory mailMonitorRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        // 自动确认模式，确保消息快速处理
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 邮箱监控建议单线程消费（或并发消费者数较低），以确保监控数据的顺序性和稳定性
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        // 可以根据需要设置预取数量，避免消费者积压消息
        factory.setPrefetchCount(1);
        return factory;
    }


}

