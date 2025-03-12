/**
 * File Name: PostUserSignController.java
 * Description: 用户登录注册控制器，处理用户验证码发送和验证相关请求
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * 提供用户登录和注册过程中的验证码发送和验证功能，支持邮箱验证码登录和新用户注册
 */
package org.example.serviceuser.user.sign.controller;

import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.user.sign.enums.PostUserSignEnum;
import org.example.serviceuser.user.sign.request.RegisterRequest;
import org.example.serviceuser.user.sign.request.VerifySignVerificationCodeRequest;
import org.example.serviceuser.user.sign.service.PostUserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 用户登录注册控制器
 * 处理用户通过邮箱验证码进行登录和注册的相关请求
 */
@RestController
@RequestMapping("user/sign")
public class PostUserSignController {

    private final PostUserSignService postUserSignService;

    /**
     * 构造函数，通过依赖注入获取用户登录注册服务
     *
     * @param postUserSignService 用户登录注册服务接口
     */
    @Autowired
    public PostUserSignController(PostUserSignService postUserSignService) {
        this.postUserSignService = postUserSignService;
    }

    /**
     * 发送登录/注册验证码
     * 向指定邮箱发送验证码，用于用户登录或注册验证
     *
     * @param mail 用户邮箱地址
     * @return ApiResponse 返回验证码发送结果，200表示成功，400表示失败
     */
    @PostMapping("sendSignVerificationCode")
    public ApiResponse sendSignVerificationCode(@RequestParam("mail") String mail) {
        boolean success = postUserSignService.sendSignVerificationCode(mail);
        if (success) {
            return new ApiResponse(200, "验证码获取成功");
        } else
            return new ApiResponse(400, "验证码获取失败");
    }

    /**
     * 验证登录/注册验证码
     * 验证用户提交的邮箱验证码，根据验证结果执行登录或提示注册
     *
     * @param request 包含邮箱和验证码的请求对象
     * @return ApiResponse 返回验证结果
     * - 200: 验证成功且用户已注册，可直接登录
     * - 201: 验证成功但用户未注册，需要完成注册流程
     * - 400: 验证失败，验证码错误或已过期
     */
    @PostMapping("verifySignVerificationCode")
    public ApiResponse verifySignVerificationCode(@RequestBody @Valid VerifySignVerificationCodeRequest request) {
        Pair<PostUserSignEnum, Object> result = postUserSignService.verifySignVerificationCode(request);

        PostUserSignEnum status = result.getFirst();
        switch (status) {
            case SUCCESS_LOGIN -> {
                return new ApiResponse(200, status.getMessage(), result.getSecond());
            }
            case SUCCESS_BUT_NOT_REGISTERED -> {
                return new ApiResponse(201, status.getMessage(), result.getSecond());
            }
            default -> {
                return new ApiResponse(400, status.getMessage());
            }
        }

    }

    @PostMapping("register")
    public ApiResponse register(@RequestBody @Valid RegisterRequest request) {
        Pair<PostUserSignEnum, Object> result = postUserSignService.register(request);
        PostUserSignEnum status = result.getFirst();
        switch (status) {
            case SUCCESS_REGISTER -> {
                return new ApiResponse(200, status.getMessage(), result.getSecond());
            }
            case NO_REGISTER_REQUEST, USERNAME_ALREADY_EXISTS -> {
                return new ApiResponse(400, status.getMessage());
            }
            default -> {
                return new ApiResponse(401, status.getMessage());
            }
        }
    }
}
