/**
 * File Name: PostUserSignEnum.java
 * Description: 用户登录注册状态枚举类，定义验证码验证后的不同状态
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * 用于表示用户验证码验证后的不同状态，包括登录成功、验证成功但未注册、验证码错误等
 */
package org.example.serviceuser.user.sign.enums;

import lombok.Getter;

/**
 * 用户登录注册状态枚举
 * 定义验证码验证后的不同状态及对应的提示信息
 */
@Getter
public enum PostUserSignEnum {
    /**
     * 验证成功且用户已注册，可直接登录
     */
    SUCCESS_LOGIN("成功登录"),

    /**
     * 验证成功但用户未注册，需要完成注册流程
     */
    SUCCESS_BUT_NOT_REGISTERED("成功但未注册"),

    /**
     * 验证失败，验证码错误或已过期
     */
    INVALID_VERIFICATION_CODE("验证码错误，请重新验证"),

    /**
     * 注册成功
     */
    SUCCESS_REGISTER("成功注册"),

    /**
     * 用户未发起注册请求
     */
    NO_REGISTER_REQUEST("未发起注册请求"),

    /**
     * 注册失败
     */
    REGISTER_FAILED("注册失败，请稍后重试"),

    /**
     * 用户名已被注册
     */
    USERNAME_ALREADY_EXISTS("用户名已被注册，请更换其他用户名"),

    /**
     * 账号不存在
     */
    ACCOUNT_NOT_FOUND("账号不存在"),

    /**
     * 密码错误
     */
    PASSWORD_INCORRECT("密码错误");

    /**
     * 状态对应的提示信息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param message 状态对应的提示信息
     */
    PostUserSignEnum(String message) {
        this.message = message;
    }
}
