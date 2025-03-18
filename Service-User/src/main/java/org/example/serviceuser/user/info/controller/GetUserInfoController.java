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
import org.example.serviceuser.user.info.service.GetUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/info")
public class GetUserInfoController {

    @Autowired
    private GetUserInfoService getUserInfoService;

    /**
     * 获取用户名和头像信息
     * 
     * @param userId 从请求头中获取的用户ID
     * @return 包含用户名和头像的响应
     */
    @GetMapping("UserNameAndAvtar")
    public ApiResponse UserNameAndAvtar(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }
        
        // 调用服务获取用户信息
        UserInfoDTO userInfo = getUserInfoService.getUserNameAndAvatar(userId);
        
        // 检查用户是否存在
        if (userInfo == null) {
            return new ApiResponse(404, "用户不存在");
        }
        
        // 返回用户信息
        return new ApiResponse(200, "获取成功", userInfo);
    }
}
