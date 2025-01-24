/**
 * File Name: PutUserAdminService.java
 * Description: 用户管理系统-管理员更新用户服务接口
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 提供管理员更新用户信息的服务接口，包括更新密码和更新用户基本信息功能
 */
package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.enums.PutUserAdminEnum;
import org.example.serviceuser.admin.request.UpdateUserRequest;

public interface PutUserAdminService {
    /**
     * 修改用户密码
     * @param id 用户ID
     * @param password 新密码
     * @return PutUserAdminEnum 更新结果状态枚举
     */
    PutUserAdminEnum UpdatePassword(Long id, String password);

    /**
     * 更新用户基本信息
     * @param updateUserRequest 用户更新信息请求对象
     * @return PutUserAdminEnum 更新结果状态枚举
     */
    PutUserAdminEnum updateUser(UpdateUserRequest updateUserRequest);
}
