/**
 * File Name: GetUserAccountController.java
 * Description: 获取用户账户信息的控制器
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供用户账户信息的API接口
 */
package org.example.serviceuser.user.account.controller;

import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.user.account.dto.UserAccountDTO;
import org.example.serviceuser.user.account.service.GetUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/account")
public class GetUserAccountController {

    private final GetUserAccountService getUserAccountService;

    @Autowired
    public GetUserAccountController(GetUserAccountService getUserAccountService) {
        this.getUserAccountService = getUserAccountService;
    }

    /**
     * 获取用户名和邮箱信息以及用户名剩余修改次数
     *
     * @param userId 从请求头中获取的用户ID
     * @return 包含用户名、邮箱和剩余修改次数的响应
     */
    @GetMapping("UsernameAndEmail")
    public ApiResponse getUsernameAndEmail(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务获取用户账户信息
        UserAccountDTO userAccount = getUserAccountService.getUsernameAndEmail(userId);

        // 检查用户是否存在
        if (userAccount == null) {
            return new ApiResponse(404, "用户不存在");
        }

        // 返回用户账户信息（包含用户名、邮箱和剩余修改次数）
        return new ApiResponse(200, "获取成功", userAccount);
    }

    /**
     * 检测用户密码是否为空（null或空字符串）
     *
     * @param userId 从请求头中获取的用户ID
     * @return 包含检测结果的响应(true表示密码为空 ， false表示密码不为空)
     */
    @GetMapping("checkPasswordEmpty")
    public ApiResponse checkPasswordEmpty(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务检查密码是否为空
        Boolean isPasswordEmpty = getUserAccountService.isPasswordEmpty(userId);

        // 检查用户是否存在
        if (isPasswordEmpty == null) {
            return new ApiResponse(404, "用户不存在");
        }

        // 返回检测结果
        return new ApiResponse(200, "检测成功,true:用户名未被初始化,false:用户名已被初始化", isPasswordEmpty);
    }

    /**
     * 检查用户是否绑定GitHub账号
     *
     * @param userId 从请求头中获取的用户ID
     * @return 包含检测结果的响应(true表示已绑定 ， false表示未绑定)
     */
    @GetMapping("isGithubBound")
    public ApiResponse isGithubBound(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务检查是否绑定GitHub
        Boolean isGithubBound = getUserAccountService.isGithubBound(userId);

        // 检查用户是否存在
        if (isGithubBound == null) {
            return new ApiResponse(404, "用户不存在");
        }

        // 返回检测结果
        return new ApiResponse(200, "检测成功,true:已绑定GitHub,false:未绑定GitHub", isGithubBound);
    }
}