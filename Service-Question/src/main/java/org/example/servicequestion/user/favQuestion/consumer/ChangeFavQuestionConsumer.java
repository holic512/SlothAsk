/**
 * File Name: ChangeFavQuestion.java
 * Description: RabbitMQ 消费者类，用于监听收藏队列并将接收到的消息缓存至内存队列，由批处理逻辑统一入库。
 * Author: holic512
 * Created Date: 2025-04-28
 * Version: 1.0
 * Usage:
 * 自动被 Spring 管理，通过 @RabbitListener 注解监听 fav.queue 队列，
 * 消息消费后会解析为 FavoriteMessage 并加入 MessageBuffer，供后续批量入库使用。
 */

package org.example.servicequestion.user.favQuestion.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.servicequestion.user.favQuestion.dto.FavoriteMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangeFavQuestionConsumer {

    private final MessageBuffer messageBuffer;
    private final ObjectMapper objectMapper;

    @Autowired
    ChangeFavQuestionConsumer(MessageBuffer messageBuffer,
                              ObjectMapper objectMapper) {
        this.messageBuffer = messageBuffer;
        this.objectMapper = objectMapper;
    }

    public static String removeSurroundingQuotes(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.startsWith("\"") && str.endsWith("\"") && str.length() >= 2) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    @RabbitListener(queues = "fav.queue", ackMode = "MANUAL")
    public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {


        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String body = new String(message.getBody());

            FavoriteMessage msg = objectMapper.readValue(body, FavoriteMessage.class);
            messageBuffer.add(msg);

            // 设置信息插入功能
            messageBuffer.setDeliveryTagAndChannel(deliveryTag, channel);
        } catch (Exception e) {
            System.out.println("出现异常" + e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
