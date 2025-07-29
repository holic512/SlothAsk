/**
 * File Name: RedisConfig.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-27
 * Version: 1.0
 * Usage:
 * 用于 basemessage 的广播 的 配置
 */
package org.example.servicenotification.baseMessage.redis;

import lombok.RequiredArgsConstructor;
import org.example.servicecommon.redisKey.BaseMessageKey;
import org.example.servicenotification.baseMessage.listener.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class BaseMessageRedisConfig {

    private final RedisMessageSubscriber redisMessageListener;

    /**
     * 广播频道名称常量
     */
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(BaseMessageKey.MESSAGE_BROADCAST_CHANNEL);
    }


    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisMessageListener, new ChannelTopic(BaseMessageKey.MESSAGE_BROADCAST_CHANNEL));
        return container;
    }

    // @Bean
    // public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
    //     RedisTemplate<String, String> template = new RedisTemplate<>();
    //     template.setConnectionFactory(factory);
    //     template.setKeySerializer(new StringRedisSerializer());
    //     template.setValueSerializer(new StringRedisSerializer());
    //     return template;
    // }

}
