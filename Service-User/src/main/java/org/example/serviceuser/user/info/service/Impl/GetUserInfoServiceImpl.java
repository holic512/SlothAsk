/**
 * File Name: GetUserInfoServiceImpl.java
 * Description: 获取用户信息的服务实现
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供获取用户信息的方法实现
 */
package org.example.serviceuser.user.info.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.serviceuser.common.UserAvatarService;
import org.example.serviceuser.config.Redis.RedisConstants;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserProfile;
import org.example.serviceuser.user.info.dto.UserInfoDTO;
import org.example.serviceuser.user.info.dto.UserProfileDTO;
import org.example.serviceuser.user.info.mapper.UserInfoUserMapper;
import org.example.serviceuser.user.info.mapper.UserInfoUserProfileMapper;
import org.example.serviceuser.user.info.service.GetUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GetUserInfoServiceImpl implements GetUserInfoService {

    private final UserInfoUserMapper userMapper;
    private final UserInfoUserProfileMapper userProfileMapper;
    private final UserAvatarService userAvatarService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public GetUserInfoServiceImpl(UserAvatarService userAvatarService,
                                  UserInfoUserMapper userMapper, 
                                  UserInfoUserProfileMapper userProfileMapper,
                                  StringRedisTemplate redisTemplate,
                                  ObjectMapper objectMapper) {
        this.userAvatarService = userAvatarService;
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserInfoDTO getUserNameAndAvatar(Long userId) {
        // 参数校验
        if (userId == null) {
            return null;
        }

        // 尝试从缓存获取用户信息
        String cacheKey = RedisConstants.User.USER_INFO_PREFIX + userId;
        String cachedUserInfo = redisTemplate.opsForValue().get(cacheKey);
        
        if (StringUtils.hasText(cachedUserInfo)) {
            try {
                return objectMapper.readValue(cachedUserInfo, UserInfoDTO.class);
            } catch (JsonProcessingException e) {
                // 缓存解析失败，删除缓存并继续查询数据库
                redisTemplate.delete(cacheKey);
            }
        }

        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }

        // 获取用户资料信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        
        String username = user.getUsername();
        String nickname;
        String realAvatarUrl = null;
        
        if (userProfile == null) {
            // 如果没有资料信息，则昵称使用用户名
            nickname = username;
        } else {
            // 获取昵称，如果昵称为空则使用用户名
            nickname = userProfile.getNickname();
            if (nickname == null || nickname.trim().isEmpty()) {
                nickname = username;
            }
            
            // 获取头像的真实URL
            String avatarUrl = userProfile.getAvatar();
            realAvatarUrl = userAvatarService.getUserAvatar(avatarUrl);
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO(username, nickname, realAvatarUrl);
        
        // 将结果缓存到Redis
        try {
            String userInfoJson = objectMapper.writeValueAsString(userInfoDTO);
            redisTemplate.opsForValue().set(cacheKey, userInfoJson, RedisConstants.User.getUserInfoTtlWithJitter());
        } catch (JsonProcessingException e) {
            // 缓存失败不影响业务逻辑，记录日志即可
            System.err.println("Failed to cache user info for userId: " + userId + ", error: " + e.getMessage());
        }

        return userInfoDTO;
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        // 参数校验
        if (userId == null) {
            return null;
        }

        // 获取用户资料
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return null;
        }

        // 获取头像的真实URL/当用户的头像存储在系统库的时候
        String avatarUrl = userProfile.getAvatar();
        String realAvatarUrl = userAvatarService.getUserAvatar(avatarUrl);
        userProfile.setAvatar(realAvatarUrl);
        
        // 转换为DTO返回
        return UserProfileDTO.fromEntity(userProfile);
    }
}
