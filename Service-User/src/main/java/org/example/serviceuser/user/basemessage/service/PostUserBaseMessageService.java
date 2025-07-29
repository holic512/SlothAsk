/**
 * File Name: PostUserBaseMessageService.java
 * Description: 用户基础消息POST操作服务接口
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * 定义用户基础消息POST操作的服务方法
 */
package org.example.serviceuser.user.basemessage.service;

public interface PostUserBaseMessageService {
    
    /**
     * 标记指定消息为已读
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return true表示操作成功，false表示消息不存在或无权限操作
     */
    boolean markMessageAsRead(Long userId, Long messageId);
}