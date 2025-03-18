/**
 * File Name: DeleteUserInfoServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.info.service.Impl;

import org.example.serviceuser.user.info.mapper.UserInfoUserProfileMapper;
import org.example.serviceuser.user.info.service.DeleteUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserInfoServiceImpl implements DeleteUserInfoService {

    private final UserInfoUserProfileMapper userInfoUserProfileMapper;

    @Autowired
    public DeleteUserInfoServiceImpl(UserInfoUserProfileMapper userInfoUserProfileMapper) {
        this.userInfoUserProfileMapper = userInfoUserProfileMapper;
    }


    @Override
    public void deleteAvatar(Long user_id) {
        userInfoUserProfileMapper.deleteAvatar(user_id);
    }
}
