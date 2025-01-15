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

import cn.dev33.satoken.stp.SaTokenInfo;
import com.thoughtworks.xstream.converters.time.LocalTimeConverter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.example.serviceadmin.auth.enmus.LoginEnum;
import org.example.serviceadmin.auth.mapper.AuthAdminUserMapper;
import org.example.serviceadmin.auth.service.PostAuthService;
import org.example.serviceadmin.entiy.AdminUser;
import org.example.serviceadmin.util.GetClientIp;
import org.example.serviceadmin.util.SCryptUtil;
import org.example.serviceadmin.util.StpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostAuthServiceImpl implements PostAuthService {

    private final AuthAdminUserMapper authAdminUserMapper;

    @Autowired
    public PostAuthServiceImpl(AuthAdminUserMapper authAdminUserMapper) {
        this.authAdminUserMapper = authAdminUserMapper;
    }

    @Override
    public Pair<LoginEnum, SaTokenInfo> login(String username, String password, HttpServletRequest request) {

        // 查询用户名的 密码
        String ReallyPassword = authAdminUserMapper.selectPasswordByUserName(username);

        // 当密码为空 证明没有这个用户
        if (ReallyPassword == null || ReallyPassword.isEmpty()) {
            return  Pair.of(LoginEnum.UserNotFound, null);
        }

        // 比对密码
        if (!SCryptUtil.verifyPassword(password, ReallyPassword)) {
            return Pair.of(LoginEnum.PasswordError, null);
        }

        // 登录成功

        // 获取用户实例
        AdminUser adminUser = authAdminUserMapper.selectByUsername(username);

        // 修改用户登录ip,时间,次数
        adminUser.setLastLoginIp(GetClientIp.getClientIp(request));
        adminUser.setLastLoginTime(LocalDateTime.now());
        adminUser.setLoginCount(adminUser.getLoginCount() + 1);
        authAdminUserMapper.updateById(adminUser);

        // 登录
        StpKit.ADMIN.login(adminUser.getId());
        SaTokenInfo tokenInfo = StpKit.ADMIN.getTokenInfo();

        return Pair.of(LoginEnum.Success, tokenInfo);

    }
}
