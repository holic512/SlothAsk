/**
 * File Name: PutUserInfoServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.info.service.Impl;

import java.util.Map;

import org.example.serviceuser.entity.UserProfile;
import org.example.serviceuser.feign.ServiceImageFeign;
import org.example.serviceuser.user.info.dto.UserProfileDTO;
import org.example.serviceuser.user.info.mapper.UserInfoUserProfileMapper;
import org.example.serviceuser.user.info.service.PutUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PutUserInfoServiceImpl implements PutUserInfoService {

    @Autowired
    private ServiceImageFeign serviceImageFeign;

    @Autowired
    private UserInfoUserProfileMapper userInfoUserProfileMapper;

    @Override
    public Pair<Boolean, String> updateAvatar(Long userId, MultipartFile file) {
        // 参数校验
        if (userId == null) {
            return Pair.of(false, "用户id不能为空");
        }
        if (file == null || file.isEmpty()) {
            return Pair.of(false, "文件不能为空");
        }

        try {
            // 1. 上传图片到图片服务
            Map<String, String> uploadResult = serviceImageFeign.uploadImage(file);
            String fileName = uploadResult.get("fileName");
            String previewUrl = uploadResult.get("previewUrl");

            // 2. 将文件名称添加#后存入用户信息表
            String avatarFileName = "#" + fileName;
            userInfoUserProfileMapper.updateUserAvatar(userId, avatarFileName);

            return Pair.of(true, previewUrl);
        } catch (Exception e) {
            return Pair.of(false, "头像更新失败");
        }
    }
    
    @Override
    public boolean updateUserProfile(Long userId, UserProfileDTO userProfileDTO) {
        // 参数校验
        if (userId == null || userProfileDTO == null) {
            return false;
        }
        
        try {
            // 查询用户资料是否存在
            UserProfile existingProfile = userInfoUserProfileMapper.selectByUserId(userId);
            if (existingProfile == null) {
                return false;
            }
            
            // 将DTO中的数据复制到实体对象
            userProfileDTO.toEntity(existingProfile);
            
            // 设置用户ID，确保更新的是正确的用户
            existingProfile.setUserId(userId);
            
            // 执行更新操作
            int rows = userInfoUserProfileMapper.updateUserProfile(existingProfile);
            
            // 返回更新结果
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
