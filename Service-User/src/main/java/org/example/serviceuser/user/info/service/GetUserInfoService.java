/**
 * File Name: GetUserInfoService.java
 * Description: 获取用户信息的服务接口
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供获取用户信息的方法
 */
package org.example.serviceuser.user.info.service;

import org.example.serviceuser.user.info.dto.UserInfoDTO;

public interface GetUserInfoService {
    
    /**
     * 获取用户名和头像
     * 
     * @param userId 用户ID
     * @return 包含用户名和头像的DTO，如果用户不存在则返回null
     */
    UserInfoDTO getUserNameAndAvatar(Long userId);
}
