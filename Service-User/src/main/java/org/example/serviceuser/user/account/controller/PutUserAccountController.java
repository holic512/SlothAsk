/**
 * File Name: PutUserAccountController.java
 * Description: 更新用户账户信息的控制器
 * Author: holic512
 * Created Date: 2025-05-20
 * Version: 1.0
 * Usage:
 * 提供用户账户信息更新的API接口，包括修改用户名和邮箱
 */
package org.example.serviceuser.user.account.controller;

import jakarta.validation.Valid;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.account.request.*;
import org.example.serviceuser.user.account.service.PutUserAccountService;
import org.example.serviceuser.util.PasswordValidator;
import org.example.serviceuser.util.StpKit;
import org.example.serviceuser.util.UsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user/account")
public class PutUserAccountController {

    private final PutUserAccountService putUserAccountService;

    @Autowired
    public PutUserAccountController(PutUserAccountService putUserAccountService) {
        this.putUserAccountService = putUserAccountService;
    }

    /**
     * 更新用户名
     *
     * @param userId  从请求头中获取的用户ID
     * @param request 包含新用户名的请求对象
     * @return 更新结果响应
     */
    @PutMapping("username")
    public ApiResponse updateUsername(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid UpdateUsernameRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 用户名格式校验
        UsernameValidator.ValidationResult validationResult =
                UsernameValidator.validateUsername(request.getUsername());
        if (!validationResult.valid()) {
            return new ApiResponse(400, "用户名格式不正确: " + String.join(", ", validationResult.errors()), false);
        }

        // 检查用户名是否已存在
        boolean usernameExists = putUserAccountService.selectUsernameExists(request.getUsername());
        if (usernameExists) {
            return new ApiResponse(400, "用户名已存在", false);
        }

        // 检查用户名修改次数是否已达上限
        int remainingChanges = putUserAccountService.getRemainingUsernameChanges(userId);
        if (remainingChanges <= 0) {
            return new ApiResponse(403, "本月用户名修改次数已用完，请下个月再试", false);
        }

        // 调用服务更新用户名
        boolean success = putUserAccountService.updateUsername(userId, request.getUsername());

        // 返回更新结果
        if (success) {
            return new ApiResponse(200, "用户名更新成功，本月剩余修改次数：" + (remainingChanges - 1), true);
        } else {
            return new ApiResponse(400, "用户名更新失败", false);
        }
    }

    /**
     * 初始化用户密码
     * 仅当用户密码为null或空字符串时才能设置
     *
     * @param userId  从请求头中获取的用户ID
     * @param request 包含新密码的请求对象
     * @return 初始化结果响应
     */
    @PutMapping("initPassword")
    public ApiResponse initializePassword(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid InitializePasswordRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 密码强度校验
        PasswordValidator.ValidationResult validationResult =
                PasswordValidator.DEFAULT.validate(request.getPassword());

        if (!validationResult.valid()) {
            // 返回具体的密码校验错误信息
            return new ApiResponse(400, "密码强度不足: " + String.join(", ", validationResult.errors()), false);
        }

        // 调用服务初始化密码
        boolean success = putUserAccountService.initializePassword(userId, request.getPassword());

        // 返回初始化结果
        if (success) {
            return new ApiResponse(200, "密码设置成功", true);
        } else {
            return new ApiResponse(400, "密码已设置或用户不存在", false);
        }
    }

    /**
     * 使用旧密码验证修改密码
     *
     * @param userId  从请求头中获取的用户ID
     * @param request 包含旧密码和新密码的请求对象
     * @return 修改结果响应
     */
    @PutMapping("password")
    public ApiResponse updatePassword(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid UpdatePasswordRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 密码强度校验
        PasswordValidator.ValidationResult validationResult =
                PasswordValidator.DEFAULT.validate(request.getNewPassword());

        if (!validationResult.valid()) {
            // 返回具体的密码校验错误信息
            return new ApiResponse(400, "密码强度不足: " + String.join(", ", validationResult.errors()), false);
        }

        // 调用服务验证旧密码并更新
        int result = putUserAccountService.updatePasswordWithOldPassword(
                userId, request.getOldPassword(), request.getNewPassword());

        // 返回更新结果
        return switch (result) {
            case 1 -> {
                // 修改密码成功后 清空其token
                StpKit.USER.logout(userId);
                yield new ApiResponse(200, "密码修改成功", true);
            }
            case 0 -> new ApiResponse(400, "旧密码错误", false);
            case -1 -> new ApiResponse(400, "用户不存在", false);
            default -> new ApiResponse(500, "系统错误", false);
        };
    }

    /**
     * 发送密码重置验证码
     *
     * @param userId 从请求头中获取的用户ID
     * @return 发送结果响应
     */
    @PostMapping("sendPasswordResetCode")
    public ApiResponse sendPasswordResetCode(
            @RequestHeader(value = "X-User-Id") Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务发送密码重置验证码
        var result = putUserAccountService.sendPasswordResetCode(userId);

        // 返回发送结果
        if (result != null) {
            return new ApiResponse(200, "验证码发送成功", result);
        } else {
            return new ApiResponse(400, "验证码发送失败，请确认账号存在且已绑定邮箱", false);
        }
    }

    /**
     * 验证验证码并重置密码
     *
     * @param userId  从请求头中获取的用户ID
     * @param request 包含唯一标识符、验证码和新密码的请求对象
     * @return 重置结果响应
     */
    @PutMapping("resetPassword")
    public ApiResponse verifyCodeAndResetPassword(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid VerifyCodeResetPasswordRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 密码强度校验
        PasswordValidator.ValidationResult validationResult =
                PasswordValidator.DEFAULT.validate(request.getNewPassword());

        if (!validationResult.valid()) {
            // 返回具体的密码校验错误信息
            return new ApiResponse(400, "密码强度不足: " + String.join(", ", validationResult.errors()), false);
        }

        // 调用服务验证验证码并重置密码
        int result = putUserAccountService.verifyCodeAndResetPassword(
                userId, request.getKey(), request.getCode(), request.getNewPassword());

        // 返回重置结果
        return switch (result) {
            case 1 -> {
                // 修改密码成功后 清空其token
                StpKit.USER.logout(userId);
                yield new ApiResponse(200, "密码重置成功", true);
            }
            case 0 -> new ApiResponse(400, "验证码错误或已过期", false);
            case -1 -> new ApiResponse(400, "验证码与邮箱不匹配", false);
            case -2 -> new ApiResponse(400, "用户不存在", false);
            default -> new ApiResponse(500, "系统错误", false);
        };
    }

    /**
     * 发送邮箱更改验证码
     *
     * @param email 目标邮箱地址
     * @return 发送结果响应
     */
    @PostMapping("sendEmailChangeCode")
    public ApiResponse sendEmailChangeCode(@RequestParam("email") String email) {
        // 调用服务发送验证码
        Map<String, String> result = putUserAccountService.sendEmailChangeCode(email);

        // 返回发送结果
        if (result != null) {
            return new ApiResponse(200, "验证码发送成功", result);
        } else {
            return new ApiResponse(400, "验证码发送失败，该邮箱可能已被使用", false);
        }
    }

    /**
     * 发送验证码到用户当前绑定的邮箱
     *
     * @param userId 从请求头中获取的用户ID
     * @return 发送结果响应
     */
    @PostMapping("sendVerificationToOriginalEmail")
    public ApiResponse sendVerificationToOriginalEmail(
            @RequestHeader(value = "X-User-Id") Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务发送验证码
        Map<String, String> result = putUserAccountService.sendVerificationToOriginalEmail(userId);

        // 返回发送结果
        if (result != null) {
            return new ApiResponse(200, "验证码发送成功", result);
        } else {
            return new ApiResponse(400, "验证码发送失败，请确认账号存在且已绑定邮箱", false);
        }
    }

    /**
     * 双重验证更新邮箱
     * 需要同时验证原邮箱和新邮箱的所有权
     *
     * @param userId  从请求头中获取的用户ID
     * @param request 包含原邮箱和新邮箱验证信息的请求对象
     * @return 更新结果响应
     */
    @PutMapping("updateEmailWithTwoSteps")
    public ApiResponse updateEmailWithTwoSteps(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid UpdateEmailWithTwoStepsRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务验证并更新邮箱
        int result = putUserAccountService.verifyAndUpdateEmailWithTwoSteps(
                userId, 
                request.getOriginalUid(), 
                request.getOriginalCode(), 
                request.getNewEmail(), 
                request.getNewUid(), 
                request.getNewCode());

        // 返回更新结果
        return switch (result) {
            case 1 -> new ApiResponse(200, "邮箱更新成功", true);
            case 0 -> new ApiResponse(400, "原邮箱验证码错误", false);
            case -1 -> new ApiResponse(400, "新邮箱验证码错误", false);
            case -2 -> new ApiResponse(400, "原邮箱或新邮箱信息不匹配", false);
            case -3 -> new ApiResponse(400, "用户不存在", false);
            case -4 -> new ApiResponse(400, "新邮箱已被使用", false);
            default -> new ApiResponse(500, "系统错误", false);
        };
    }

    /**
     * 通过密码验证更新邮箱
     *
     * @param userId  从请求头中获取的用户ID
     * @param request 包含唯一标识符、新邮箱和密码的请求对象
     * @return 更新结果响应
     */
    @PutMapping("updateEmailWithPassword")
    public ApiResponse updateEmailWithPassword(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid UpdateEmailWithPasswordRequest request) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务验证密码并更新邮箱
        int result = putUserAccountService.updateEmailWithPassword(
                userId, request.getUid(), request.getEmail(), request.getCode(), request.getPassword());

        // 返回更新结果
        return switch (result) {
            case 1 -> new ApiResponse(200, "邮箱更新成功", true);
            case 0 -> new ApiResponse(400, "密码错误", false);
            case -1 -> new ApiResponse(400, "验证码无效或已过期", false);
            case -2 -> new ApiResponse(400, "邮箱不匹配或已被使用", false);
            case -3 -> new ApiResponse(400, "用户不存在", false);
            default -> new ApiResponse(500, "系统错误", false);
        };
    }

    /**
     * 解绑GitHub账号
     *
     * @param userId 从请求头中获取的用户ID
     * @return 解绑结果响应
     */
    @DeleteMapping("unbindGithub")
    public ApiResponse unbindGithub(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务解绑GitHub账号
        boolean success = putUserAccountService.unbindGithub(userId);

        // 返回解绑结果
        if (success) {
            return new ApiResponse(200, "解绑GitHub账号成功", true);
        } else {
            return new ApiResponse(400, "解绑失败，可能未绑定GitHub账号或用户不存在", false);
        }
    }
}