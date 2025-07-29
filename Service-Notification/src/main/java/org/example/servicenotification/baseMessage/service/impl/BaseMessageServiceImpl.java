/**
 * File Name: BaseMessageServiceImpl.java
 * Description: 用户消息服务实现类
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 */
package org.example.servicenotification.baseMessage.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.baseMessage.message.BaseMessageMessage;
import org.example.servicecommon.entity.BaseMessage;
import org.example.servicenotification.baseMessage.mapper.BMBaseMessageMapper;
import org.example.servicenotification.baseMessage.service.BaseMessageService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseMessageServiceImpl implements BaseMessageService {

    private final BMBaseMessageMapper baseMessageMapper;
    private final ObjectMapper objectMapper;

    @Override
    public BaseMessage saveMessage(BaseMessageMessage<?> messageMessage) {
        try {
            log.info("开始保存用户消息: userId={}, type={}", messageMessage.getUserId(), messageMessage.getType());

            // 转换为数据库实体
            BaseMessage baseMessage = convertToEntity(messageMessage);

            // 保存到数据库
            int result = baseMessageMapper.insert(baseMessage);

            // 保存成功
            if (result > 0) {
                log.info("用户消息保存成功: userId={}, type={}, messageId={}",
                        messageMessage.getUserId(), messageMessage.getType(), baseMessage.getId());

                return baseMessage;
            } else {
                log.error("用户消息保存失败: userId={}, type={}",
                        messageMessage.getUserId(), messageMessage.getType());
                throw new RuntimeException("用户消息保存失败");
            }

        } catch (Exception e) {
            log.error("保存用户消息时发生异常: userId={}, type={}, error={}",
                    messageMessage.getUserId(), messageMessage.getType(), e.getMessage(), e);
            throw new RuntimeException("保存用户消息失败", e);
        }
    }

    @Override
    public BaseMessage convertToEntity(BaseMessageMessage<?> messageMessage) {
        try {
            BaseMessage baseMessage = new BaseMessage();
            baseMessage.setUserId(messageMessage.getUserId());
            baseMessage.setType(messageMessage.getType());
            baseMessage.setReadStatus(messageMessage.getReadStatus() != null ? messageMessage.getReadStatus() : 0); // 默认未读

            // 将messageData序列化为JSON字符串
            if (messageMessage.getMessageData() != null) {
                String messageDataJson = objectMapper.writeValueAsString(messageMessage.getMessageData());
                baseMessage.setMessageData(messageDataJson);
            }

            return baseMessage;
        } catch (Exception e) {
            log.error("转换消息实体时发生异常: {}", e.getMessage(), e);
            throw new RuntimeException("转换消息实体失败", e);
        }
    }
}