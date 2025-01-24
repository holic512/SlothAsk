/**
 * File Name: PostUserAdminService.java
 * Description: 用户管理系统-管理员添加用户服务接口
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 提供管理员添加新用户的服务接口，包括基本信息和个人资料的添加
 */
package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.enums.PostUserAdminEnum;
import org.example.serviceuser.admin.request.AddUserRequest;

public interface PostUserAdminService {
    /**
     * 添加新用户
     * @param addUserRequest 新用户的信息请求对象
     * @return PostUserAdminEnum 添加结果状态枚举
     */
    PostUserAdminEnum addUser(AddUserRequest addUserRequest);
}
