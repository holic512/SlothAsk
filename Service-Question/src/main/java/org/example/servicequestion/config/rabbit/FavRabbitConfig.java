/**
 * File Name: FavRabbitConfig.java
 * Description: 收藏功能的 RabbitMQ 配置类，定义了收藏消息的队列、交换机、路由键以及发送逻辑。
 * Author: holic512
 * Created Date: 2025-04-24
 * Version: 1.0
 * Usage:
 * 自动加载配置后，可通过调用 sendMessage 方法发送 FavoriteMessage 类型的收藏消息，
 * 消息将被路由到绑定的收藏队列，供消费者处理业务逻辑。
 */

package org.example.servicequestion.config.rabbit;


import org.example.servicequestion.user.favQuestion.dto.FavoriteMessage;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 收藏功能相关的 RabbitMQ 配置
 */
@Configuration
public class FavRabbitConfig {

    // 收藏队列名
    public static final String FAV_QUEUE = "fav.queue";
    // 收藏交换机名
    public static final String FAV_EXCHANGE = "fav.exchange";
    // 收藏路由键
    public static final String FAV_ROUTE_KEY = "fav.route";

    /**
     * 声明收藏队列（持久化）
     */
    @Bean
    public Queue favQueue() {
        return new Queue(FAV_QUEUE, true);
    }

    /**
     * 声明收藏交换机
     */
    @Bean
    public DirectExchange favExchange() {
        return new DirectExchange(FAV_EXCHANGE, true, false);
    }

    /**
     * 收藏队列与交换机绑定
     */
    @Bean
    public Binding favBinding(Queue favQueue, DirectExchange favExchange) {
        return BindingBuilder.bind(favQueue).to(favExchange).with(FAV_ROUTE_KEY);
    }

    /**
     * 发送消息到指定队列
     *
     * @param rabbitTemplate  发送消息的模板
     * @param favoriteMessage 需要发送的消息内容
     */
    public void sendMessage(RabbitTemplate rabbitTemplate, FavoriteMessage favoriteMessage) {
        try {

            rabbitTemplate.convertAndSend(FAV_EXCHANGE, FAV_ROUTE_KEY, favoriteMessage);
        } catch (Exception e) {
            throw new RuntimeException("发送收藏消息失败", e);
        }
    }

}


