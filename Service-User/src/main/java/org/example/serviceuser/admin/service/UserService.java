package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.dto.ResultDto;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.dto.UserProfileDto;

import java.util.List;

public interface UserService {

    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    String UpdatePassword(int id, String password);


    // 更新用户基本信息
    int updateUser(UserDto userDto);

    // 更新用户资料
    int updateUserProfile(UserProfileDto userProfileDto);

    int deleteUser(int id);

    int deleteUsersBatch(List<Integer> ids);
}
