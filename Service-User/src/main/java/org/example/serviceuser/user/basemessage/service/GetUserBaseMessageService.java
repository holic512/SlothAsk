/**
 * File Name: GetUserBaseMessageService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-07-29
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.basemessage.service;

import org.example.serviceuser.user.basemessage.dto.BaseMessagePageQueryDto;
import org.example.serviceuser.user.basemessage.dto.BaseMessagePageResultDto;
import org.example.serviceuser.user.basemessage.dto.BaseMessageResponseDto;

import java.util.List;

public interface GetUserBaseMessageService {
    
    /**
     * 检查用户最新创建的前十条消息中是否存在未读消息
     * @param userId 用户ID
     * @return true表示存在未读消息，false表示不存在未读消息
     */
    boolean hasUnreadMessagesInLatestTen(Long userId);
    
    /**
     * 获取用户最新创建的前十条消息
     * @param userId 用户ID
     * @return 用户最新的前十条消息列表（不包含updateTime字段）
     */
    List<BaseMessageResponseDto> getLatestTenMessages(Long userId);
    
    /**
     * 分页查询用户消息（支持多种过滤条件）
     * @param userId 用户ID
     * @param queryDto 分页查询条件
     * @return 分页查询结果
     */
    BaseMessagePageResultDto getMessagesByPage(Long userId, BaseMessagePageQueryDto queryDto);
    
    /**
     * 批量已读用户最新创建的前十条消息
     * @param userId 用户ID
     * @return 更新的消息数量
     */
    int markLatestTenMessagesAsRead(Long userId);
    
    /**
     * 批量已读用户的全部未读消息
     * @param userId 用户ID
     * @return 更新的消息数量
     */
    int markAllMessagesAsRead(Long userId);
}
