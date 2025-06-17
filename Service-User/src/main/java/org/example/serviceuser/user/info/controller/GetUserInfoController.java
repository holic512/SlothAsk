/**
 * File Name: GetUserInfoController.java
 * Description: 获取用户信息的控制器
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供用户信息的API接口
 */
package org.example.serviceuser.user.info.controller;

import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.user.info.dto.UserInfoDTO;
import org.example.serviceuser.user.info.dto.UserProfileDTO;
import org.example.serviceuser.user.info.service.GetUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/info")
public class GetUserInfoController {


    private final GetUserInfoService getUserInfoService;

    @Autowired
    public GetUserInfoController(GetUserInfoService getUserInfoService) {
        this.getUserInfoService = getUserInfoService;
    }

    /**
     * 获取用户基本信息（用户名、昵称、头像URL）
     * 用于顶部导航栏显示
     *
     * @param userId 从请求头中获取的用户ID
     * @return 包含用户名、昵称和头像URL的响应
     */
    @GetMapping("UserBasicInfo")
    public ApiResponse getUserBasicInfo(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }

        // 调用服务获取用户基本信息
        UserInfoDTO userInfo = getUserInfoService.getUserNameAndAvatar(userId);

        // 检查用户是否存在
        if (userInfo == null) {
            return new ApiResponse(404, "用户不存在");
        }

        // 返回用户基本信息
        return new ApiResponse(200, "获取用户基本信息成功", userInfo);
    }

    /**
     * 获取用户完整资料
     *
     * @param userId 从请求头中获取的用户ID
     * @return 包含用户完整资料的响应
     */
    @GetMapping("/UserProfile")
    public ApiResponse GetUserProfile(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }
        
        // 调用服务获取用户资料
        UserProfileDTO userProfile = getUserInfoService.getUserProfile(userId);

        if (userProfile != null) {
            return new ApiResponse(200, "查询用户信息成功", userProfile);
        }
        return new ApiResponse(400, "查询用户信息失败");
    }
}
