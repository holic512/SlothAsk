/**
 * File Name: BaseMessageConsumer.java
 * Description: 用户消息消费者，接收RabbitMQ消息并保存到数据库
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 */
package org.example.servicenotification.baseMessage.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.baseMessage.message.BaseMessageMessage;
import org.example.servicecommon.entity.BaseMessage;
import org.example.servicenotification.baseMessage.config.BaseMessageMqConfig;
import org.example.servicenotification.baseMessage.publisher.RedisMessagePublisher;
import org.example.servicenotification.baseMessage.service.BaseMessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BaseMessageConsumer {

    private final BaseMessageService baseMessageService;
    private final RedisMessagePublisher redisMessagePublisher;
    private final ObjectMapper objectMapper;

    /**
     * 消费用户消息队列
     * @param messageMessage 接收到的消息
     */
    @RabbitListener(queues = BaseMessageMqConfig.MESSAGE_QUEUE)
    public void handleBaseMessage(BaseMessageMessage<?> messageMessage) {
        try {
            log.info("接收到用户消息: userId={}, type={}", messageMessage.getUserId(), messageMessage.getType());
            
            // 调用服务层保存消息
            BaseMessage savedMessage = baseMessageService.saveMessage(messageMessage);

            // 将完整的消息体通过Redis广播发布出去
            String messageJson = objectMapper.writeValueAsString(savedMessage);
            redisMessagePublisher.publish(messageJson);
            
            log.info("消息已通过Redis广播发布: messageId={}", savedMessage.getId());

            
        } catch (Exception e) {
            log.error("处理用户消息时发生异常: userId={}, type={}, error={}", 
                messageMessage.getUserId(), messageMessage.getType(), e.getMessage(), e);
            // 这里可以根据业务需求决定是否重新抛出异常进行重试
            throw new RuntimeException("处理用户消息失败", e);
        }
    }
}