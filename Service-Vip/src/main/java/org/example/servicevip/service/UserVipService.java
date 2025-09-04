/**
 * File Name: UserVipService.java
 * Description: 用户VIP信息业务逻辑层接口
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicevip.service;

import org.example.servicecommon.entity.UserVip;
import org.example.servicevip.dto.VipInfoDTO;

public interface UserVipService {
    
    /**
     * 根据用户ID查询VIP信息
     * @param userId 用户ID
     * @return UserVip VIP信息，如果用户没有VIP则返回null
     */
    UserVip getVipByUserId(Long userId);
    
    /**
     * 检查用户VIP是否生效
     * @param userId 用户ID
     * @return boolean VIP是否生效
     */
    boolean isVipActive(Long userId);
    
    /**
     * 获取用户VIP完整信息（包含isActive状态）
     * @param userId 用户ID
     * @return VipInfoDTO VIP完整信息，如果用户没有VIP则返回null
     */
    VipInfoDTO getVipInfoByUserId(Long userId);
}