/**
 * File Name: PostAuthServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.auth.service.Impl;

import org.example.serviceadmin.auth.enmus.LoginEnum;
import org.example.serviceadmin.auth.mapper.AuthAdminUserMapper;
import org.example.serviceadmin.auth.service.PostAuthService;
import org.example.serviceadmin.util.SCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostAuthServiceImpl implements PostAuthService {

    private final AuthAdminUserMapper authAdminUserMapper;

    @Autowired
    public PostAuthServiceImpl(AuthAdminUserMapper authAdminUserMapper) {
        this.authAdminUserMapper = authAdminUserMapper;
    }

    @Override
    public LoginEnum login(String username, String password) {

        // 查询用户名的 密码
        String ReallyPassword = authAdminUserMapper.selectPasswordByUserName(username);

        // 当密码为空 证明没有这个用户
        if (ReallyPassword == null || ReallyPassword.isEmpty()) {
            return LoginEnum.UserNotFound;
        }

        // 比对密码
        if (!SCryptUtil.verifyPassword(password, ReallyPassword)) {
            return LoginEnum.PasswordError;
        }

        return LoginEnum.Success;
    }
}
