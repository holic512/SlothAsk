package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.config.ApiResponse.ApiResponse;

public interface PutUserService {
    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    String UpdatePassword(int id, String password);

    // 更新用户基本信息
    ApiResponse updateUser(UserDto userDto);
}
