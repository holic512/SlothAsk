/**
 * File Name: DeleteUserServiceImpl.java
 * Description: 用户管理系统-管理员删除用户服务实现类
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 实现管理员删除用户的服务接口，包括单个删除和批量删除的具体实现
 */
package org.example.serviceuser.admin.service.impl;

import java.util.List;

import org.example.serviceuser.admin.mapper.AdminUserMapper;
import org.example.serviceuser.admin.service.DeleteUserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserAdminAdminServiceImpl implements DeleteUserAdminService {

    private final AdminUserMapper adminUserMapper;

    @Autowired
    public DeleteUserAdminAdminServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public int deleteUser(Long id) {
        return adminUserMapper.deleteUser(id);
    }

    @Override
    public boolean deleteUsersBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false; // 如果没有传入 IDs，返回删除失败a
        }

        return adminUserMapper.deleteUsersBatch(ids) > 0;
    }
}
