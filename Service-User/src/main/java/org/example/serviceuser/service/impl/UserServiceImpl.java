/**
 * File Name: UserServiceImpl.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import org.example.serviceuser.dto.PageDto;
import org.example.serviceuser.dto.ResultDto;
import org.example.serviceuser.dto.UserDto;
import org.example.serviceuser.dto.UserProfileDto;
import org.example.serviceuser.mapper.UserMapper;
import org.example.serviceuser.mapper.UserProfileMapper;
import org.example.serviceuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public String UpdatePassword(int id, String password) {
        // 调用Mapper方法，返回一个字符串（可能是空字符串或操作结果）
        int result = userMapper.updatePasswordByUserId(id, password);

        // 如果受影响的行数大于0，表示更新成功
        if (result > 0) {
            return "success";  // 更新成功
        } else {
            return "failure";  // 更新失败
        }
    }

    @Override
    public ResultDto getUsers(String keyword, Integer status, int pageNum, int pageSize) {
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 调用Mapper方法获取用户列表
        List<UserDto> userList = userMapper.getUserList(keyword, status, offset, pageSize);

        // 获取总记录数（不考虑分页）
        int total = userMapper.countUsers(keyword, status);

        // 返回结果
        return new ResultDto(200, "success", new PageDto(total, userList));
    }


    public int updateUser(UserDto userDto) {
        return userMapper.updateUser(userDto);
    }

    public int updateUserProfile(UserProfileDto userProfileDto) {
        return userProfileMapper.updateUser(userProfileDto);
    }

    @Override
    public int deleteUser(int id) {
      return userMapper.deleteById(id);
    }

    @Override
    public int deleteUsersBatch(List<Integer> ids) {
        return userMapper.deleteUsersBatch(ids);
    }

    // 随机生成密码
    private String generateRandomPassword() {
        // 可以使用密码生成工具或者随机数生成函数
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);  // 生成一个8位的随机密码
    }

}
