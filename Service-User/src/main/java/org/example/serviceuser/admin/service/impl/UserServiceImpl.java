/**
 * File Name: UserServiceImpl.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.service.impl;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.dto.UserProfileDto;
import org.example.serviceuser.admin.mapper.AdminUserMapper;
import org.example.serviceuser.admin.mapper.AdminUserProfileMapper;
import org.example.serviceuser.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AdminUserProfileMapper adminUserProfileMapper;

    @Override
    public String UpdatePassword(int id, String password) {
        // 调用Mapper方法，返回一个字符串（可能是空字符串或操作结果）
        int result = adminUserMapper.updatePasswordByUserId(id, password);

        // 如果受影响的行数大于0，表示更新成功
        if (result > 0) {
            return "success";  // 更新成功
        } else {
            return "failure";  // 更新失败
        }
    }




    public int updateUser(UserDto userDto) {
        return adminUserMapper.updateUser(userDto);
    }

    public int updateUserProfile(UserProfileDto userProfileDto) {
        return adminUserProfileMapper.updateUser(userProfileDto);
    }

    @Override
    public int deleteUser(int id) {
      return adminUserMapper.deleteById(id);
    }

    @Override
    public int deleteUsersBatch(List<Integer> ids) {
        return adminUserMapper.deleteUsersBatch(ids);
    }

    // 随机生成密码
    private String generateRandomPassword() {
        // 可以使用密码生成工具或者随机数生成函数
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);  // 生成一个8位的随机密码
    }

}
