/**
 * File Name: BaseMessageService.java
 * Description: 用户消息服务接口
 * Author: holic512
 * Created Date: 2025-06-25
 * Version: 1.0
 */
package org.example.servicenotification.baseMessage.service;

import org.example.servicecommon.baseMessage.message.BaseMessageMessage;
import org.example.servicecommon.entity.BaseMessage;

public interface BaseMessageService {

    /**
     * 保存用户消息
     * @param messageMessage 消息DTO
     * @return 保存的消息实体
     */
    BaseMessage saveMessage(BaseMessageMessage<?> messageMessage);

    /**
     * 将消息DTO转换为数据库实体
     * @param messageMessage 消息DTO
     * @return 数据库实体
     */
    BaseMessage convertToEntity(BaseMessageMessage<?> messageMessage);
}