/**
 * File Name: GetAdminServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.service.impl;

import org.example.serviceuser.admin.dto.PageDto;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.mapper.AdminUserMapper;
import org.example.serviceuser.admin.mapper.AdminUserProfileMapper;
import org.example.serviceuser.admin.service.GetUserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserAdminAdminServiceImpl implements GetUserAdminService {


    private final AdminUserMapper adminUserMapper;
    private final AdminUserProfileMapper adminUserProfileMapper;

    @Autowired
    public GetUserAdminAdminServiceImpl(AdminUserMapper adminUserMapper, AdminUserProfileMapper adminUserProfileMapper) {
        this.adminUserMapper = adminUserMapper;
        this.adminUserProfileMapper = adminUserProfileMapper;
    }

    @Override
    public PageDto getUsers(String keyword, Integer status, int pageNum, int pageSize) {

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;


        List<UserDto> userList;
        int total;

        if (status != null) {
            // 调用 可定义状态的 获取列表方法
            userList = adminUserMapper.getUserListByStatus(keyword, status, offset, pageSize);
            total = adminUserMapper.countUsersByStatus(keyword, status);
        } else {
            // 调用 可获取全部状态的用户列表 的 方法
            userList = adminUserMapper.getUserList(keyword, offset, pageSize);
            total = adminUserMapper.countUsers(keyword);
        }

        // 返回结果
        return new PageDto(total, userList);
    }
}
