package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.enums.PutUserAdminEnum;
import org.example.serviceuser.admin.request.UpdateUserRequest;
import org.example.serviceuser.config.ApiResponse.ApiResponse;

public interface PutUserAdminService {
    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    PutUserAdminEnum UpdatePassword(Long id, String password);

    // 更新用户基本信息
    PutUserAdminEnum updateUser(UpdateUserRequest updateUserRequest);
}
