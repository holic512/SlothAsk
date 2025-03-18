/**
 * File Name: PutUserInfoService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.info.service;

import org.example.serviceuser.user.info.dto.UserProfileDTO;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface PutUserInfoService {
    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param file   头像文件
     * @return Pair<Boolean, String> 分别为 是否添加成功 和  返回的 url
     */
    Pair<Boolean, String> updateAvatar(Long userId, MultipartFile file);
    
    /**
     * 更新用户个人资料
     *
     * @param userId 用户ID
     * @param userProfileDTO 用户资料数据传输对象
     * @return 是否更新成功
     */
    boolean updateUserProfile(Long userId, UserProfileDTO userProfileDTO);
}
