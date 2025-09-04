/**
 * File Name: UserVipServiceImpl.java
 * Description: 用户VIP信息业务逻辑层实现类
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicevip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.servicecommon.entity.UserVip;
import org.example.servicecommon.redisKey.VipKey;
import org.example.servicevip.dto.VipInfoDTO;
import org.example.servicevip.enums.VipType;
import org.example.servicevip.mapper.UserVipMapper;
import org.example.servicevip.service.UserVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class UserVipServiceImpl implements UserVipService {

    @Autowired
    private UserVipMapper userVipMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserVip getVipByUserId(Long userId) {
        if (userId == null) {
            return null;
        }

        // 先从Redis缓存中获取
        String cacheKey = VipKey.USER_VIP_INFO_KEY + userId;
        UserVip cachedVip = (UserVip) redisTemplate.opsForValue().get(cacheKey);
        if (cachedVip != null) {
            return cachedVip;
        }

        // 缓存中没有，从数据库查询
        QueryWrapper<UserVip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserVip userVip = userVipMapper.selectOne(queryWrapper);

        // 将查询结果缓存到Redis，设置30分钟过期时间
        if (userVip != null) {
            redisTemplate.opsForValue().set(cacheKey, userVip, 30, TimeUnit.MINUTES);
        }

        return userVip;
    }

    @Override
    public boolean isVipActive(Long userId) {
        if (userId == null) {
            return false;
        }

        // 先从Redis缓存中获取VIP状态
        String statusCacheKey = VipKey.USER_VIP_STATUS_KEY + userId;
        Boolean cachedStatus = (Boolean) redisTemplate.opsForValue().get(statusCacheKey);
        if (cachedStatus != null) {
            return cachedStatus;
        }

        UserVip userVip = getVipByUserId(userId);

        if (userVip == null) {
            // 缓存非VIP状态，设置10分钟过期时间
            redisTemplate.opsForValue().set(statusCacheKey, false, 10, TimeUnit.MINUTES);
            return false;
        }

        boolean isActive;
        // 检查是否是永久vip
        if (userVip.getVipType().equals(VipType.PERMANENT.getCode())) {
            isActive = true;
        } else {
            // 检查VIP是否在有效期内
            LocalDateTime now = LocalDateTime.now();
            isActive = userVip.getStartTime() != null &&
                    userVip.getEndTime() != null &&
                    now.isAfter(userVip.getStartTime()) &&
                    now.isBefore(userVip.getEndTime());
        }

        // 缓存VIP状态，设置10分钟过期时间
        redisTemplate.opsForValue().set(statusCacheKey, isActive, 10, TimeUnit.MINUTES);
        return isActive;
    }

    @Override
    public VipInfoDTO getVipInfoByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        
        UserVip userVip = getVipByUserId(userId);
        if (userVip == null) {
            return null;
        }
        
        // 获取VIP是否生效
        boolean isActive = isVipActive(userId);
        
        // 转换为DTO
        VipInfoDTO vipInfoDTO = new VipInfoDTO();
        vipInfoDTO.setId(userVip.getId());
        vipInfoDTO.setUserId(userVip.getUserId());
        vipInfoDTO.setVipType(userVip.getVipType());
        vipInfoDTO.setIsActive(isActive);
        
        // 格式化时间为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (userVip.getStartTime() != null) {
            vipInfoDTO.setStartTime(userVip.getStartTime().format(formatter));
        }
        if (userVip.getEndTime() != null) {
            vipInfoDTO.setEndTime(userVip.getEndTime().format(formatter));
        }
        
        return vipInfoDTO;
    }
    
    /**
     * 清除用户VIP相关缓存
     * @param userId 用户ID
     */
    public void clearVipCache(Long userId) {
        if (userId == null) {
            return;
        }
        
        String infoCacheKey = VipKey.USER_VIP_INFO_KEY + userId;
        String statusCacheKey = VipKey.USER_VIP_STATUS_KEY + userId;
        
        redisTemplate.delete(infoCacheKey);
        redisTemplate.delete(statusCacheKey);
    }
}