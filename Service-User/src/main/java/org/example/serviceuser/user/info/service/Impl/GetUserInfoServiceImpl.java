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

import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserProfile;
import org.example.serviceuser.feign.ServiceImageFeign;
import org.example.serviceuser.user.info.dto.UserInfoDTO;
import org.example.serviceuser.user.info.dto.UserProfileDTO;
import org.example.serviceuser.user.info.mapper.UserInfoUserMapper;
import org.example.serviceuser.user.info.mapper.UserInfoUserProfileMapper;
import org.example.serviceuser.user.info.service.GetUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserInfoServiceImpl implements GetUserInfoService {

    @Autowired
    private UserInfoUserMapper userMapper;

    @Autowired
    private UserInfoUserProfileMapper userProfileMapper;

    @Autowired
    private ServiceImageFeign serviceImageFeign;

    @Override
    public UserInfoDTO getUserNameAndAvatar(Long userId) {
        // 参数校验
        if (userId == null) {
            return null;
        }

        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }

        // 获取用户资料信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            // 如果没有资料信息，则返回用户名作为昵称
            return new UserInfoDTO(user.getUsername(), null);
        }

        // 获取昵称，如果昵称为空则使用用户名
        String nickname = userProfile.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = user.getUsername();
        }

        // 获取头像URL
        String avatarUrl = userProfile.getAvatar();

        // 检查头像URL是否以#开头，如果是则调用图片服务获取真实URL
        if (avatarUrl != null && avatarUrl.startsWith("#")) {
            // 去掉开头的#号，获取文件名
            String fileName = avatarUrl.substring(1);
            // 调用图片服务获取预览URL
            String realUrl = serviceImageFeign.getPreviewUrl(fileName);
            avatarUrl = realUrl;
        }

        return new UserInfoDTO(nickname, avatarUrl);
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

        // 获取头像URL
        String avatarUrl = userProfile.getAvatar();

        // 检查头像URL是否以#开头，如果是则调用图片服务获取真实URL
        if (avatarUrl != null && avatarUrl.startsWith("#")) {
            // 去掉开头的#号，获取文件名
            String fileName = avatarUrl.substring(1);
            // 调用图片服务获取预览URL
            String realUrl = serviceImageFeign.getPreviewUrl(fileName);
            userProfile.setAvatar(realUrl);
        }

        // 转换为DTO返回
        return UserProfileDTO.fromEntity(userProfile);
    }
}
