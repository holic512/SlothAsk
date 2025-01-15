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

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.example.serviceadmin.auth.dto.CaptchaResponse;
import org.example.serviceadmin.auth.dto.LoginRequest;
import org.example.serviceadmin.auth.enmus.LoginEnum;
import org.example.serviceadmin.auth.mapper.AuthAdminUserMapper;
import org.example.serviceadmin.auth.service.PostAuthService;
import org.example.serviceadmin.config.Redis.RedisConfig;
import org.example.serviceadmin.entiy.AdminUser;
import org.example.serviceadmin.util.GetClientIp;
import org.example.serviceadmin.util.SCryptUtil;
import org.example.serviceadmin.util.StpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.pig4cloud.captcha.ArithmeticCaptcha;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证服务实现类
 * 处理用户登录认证相关的业务逻辑
 *
 * @author holic512
 * @version 1.0
 * @since 2025-01-15
 */
@Service
@Slf4j
public class PostAuthServiceImpl implements PostAuthService {

    private final AuthAdminUserMapper authAdminUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LOGIN_FAIL_COUNT_PREFIX = RedisConfig.getKey() + "login:fail:count:";
    private static final String CAPTCHA_PREFIX = RedisConfig.getKey() + "login:captcha:";
    private static final int MAX_FAIL_COUNT = 5;
    private static final long LOCK_TIME = 60; // 1分钟

    @Autowired
    public PostAuthServiceImpl(AuthAdminUserMapper authAdminUserMapper,
                               RedisTemplate<String, Object> redisTemplate) {
        this.authAdminUserMapper = authAdminUserMapper;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 记录登录失败次数
     *
     * @param username 用户名
     * @return 当前失败次数
     */
    private int recordLoginFail(String username) {
        String key = LOGIN_FAIL_COUNT_PREFIX + username;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            // 第一次失败，设置过期时间
            redisTemplate.expire(key, LOCK_TIME, TimeUnit.SECONDS);
        }
        return count != null ? count.intValue() : 0;
    }

    /**
     * 检查用户是否被锁定
     *
     * @param username 用户名
     * @return true表示被锁定，false表示未被锁定
     */
    private boolean isUserLocked(String username) {
        String key = LOGIN_FAIL_COUNT_PREFIX + username;
        Object count = redisTemplate.opsForValue().get(key);
        return count != null && ((Integer) count) >= MAX_FAIL_COUNT;
    }


    @Override
    public Pair<LoginEnum, Object> login(LoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String captchaId = loginRequest.getCaptchaId();
        String captchaAnswer = loginRequest.getCaptchaAnswer();

        // 1. 首先检查账号是否被锁定
        if (isUserLocked(username)) {
            // 如果提供了验证码信息，先验证验证码
            if (captchaId != null && captchaAnswer != null) {
                // 验证码正确，继续登录流程
                if (validateCaptcha(captchaId, captchaAnswer)) {
                    // 验证码正确，清除登录失败记录
                    clearLoginFailCount(username);
                    // 继续执行登录逻辑
                    return processLogin(username, password, request);
                } else {
                    // 验证码错误，删除旧的验证码记录
                    deleteCaptcha(captchaId);
                    // 生成新的验证码
                    return Pair.of(LoginEnum.CaptchaError, generateCaptcha());
                }
            }

            // 没有提供验证码，生成新的验证码
            if (captchaId != null) {
                deleteCaptcha(captchaId);
            }

            return Pair.of(LoginEnum.AccountLocked, generateCaptcha());
        }

        // 2. 账号未锁定，正常登录流程
        return processLogin(username, password, request);
    }

    /**
     * 处理正常的登录流程
     */
    private Pair<LoginEnum, Object> processLogin(String username, String password, HttpServletRequest request) {
        // 查询用户名的密码
        String reallyPassword = authAdminUserMapper.selectPasswordByUserName(username);

        // 当密码为空证明没有这个用户
        if (reallyPassword == null || reallyPassword.isEmpty()) {
            recordLoginFail(username);
            return Pair.of(LoginEnum.UserNotFound, null);
        }

        // 比对密码
        if (!SCryptUtil.verifyPassword(password, reallyPassword)) {
            recordLoginFail(username);
            // 检查失败后是否需要验证码
            if (isUserLocked(username)) {
                return Pair.of(LoginEnum.AccountLocked, generateCaptcha());
            }
            return Pair.of(LoginEnum.PasswordError, null);
        }

        // 登录成功
        AdminUser adminUser = authAdminUserMapper.selectByUsername(username);
        updateLoginInfo(adminUser, request);
        StpKit.ADMIN.login(adminUser.getId());
        return Pair.of(LoginEnum.Success, StpKit.ADMIN.getTokenInfo());
    }


    /**
     * 清除用户的登录失败记录
     */
    private void clearLoginFailCount(String username) {
        String key = LOGIN_FAIL_COUNT_PREFIX + username;
        redisTemplate.delete(key);
    }

    /**
     * 异步更新用户登录信息
     * 包括最后登录IP、登录时间和登录次数
     *
     * @param adminUser 管理员用户对象
     * @param request   HTTP请求对象，用于获取客户端IP
     */
    @Async
    public void updateLoginInfo(AdminUser adminUser, HttpServletRequest request) {
        adminUser.setLastLoginIp(GetClientIp.getClientIp(request));
        adminUser.setLastLoginTime(LocalDateTime.now());
        adminUser.setLoginCount(adminUser.getLoginCount() + 1);
        authAdminUserMapper.updateById(adminUser);
    }

    /**
     * 生成验证码
     *
     * @return 验证码响应对象
     */
    public CaptchaResponse generateCaptcha() {
        // 创建算术验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);  // 两位数运算

        String captchaId = UUID.randomUUID().toString();

        // 保存验证码结果到Redis
        redisTemplate.opsForValue().set(
                CAPTCHA_PREFIX + captchaId,
                captcha.text(), // 获取运算结果
                LOCK_TIME,
                TimeUnit.SECONDS
        );

        return new CaptchaResponse(captchaId, captcha.toBase64());
    }


    /**
     * 验证验证码
     *
     * @param captchaId  验证码ID
     * @param userAnswer 用户输入的答案
     * @return 是否正确
     */
    private boolean validateCaptcha(String captchaId, String userAnswer) {
        String key = CAPTCHA_PREFIX + captchaId;
        String correctAnswer = (String) redisTemplate.opsForValue().get(key);

        if (correctAnswer == null) {
            return false;
        }

        // 验证后删除验证码
        redisTemplate.delete(key);

        return correctAnswer.equals(userAnswer);
    }

    /**
     * 删除验证码记录
     *
     * @param captchaId 验证码ID
     */
    private void deleteCaptcha(String captchaId) {
        if (captchaId != null && !captchaId.isEmpty()) {
            String key = CAPTCHA_PREFIX + captchaId;
            redisTemplate.delete(key);
        }
    }
}
