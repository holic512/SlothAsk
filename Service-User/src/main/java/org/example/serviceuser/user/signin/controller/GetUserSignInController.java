/**
 * File Name: GetUserSignInController.java
 * Description: 用户签到查询控制器
 * Author: holic512
 * Created Date: 2025-06-20
 * Version: 1.0
 * Usage:
 * 处理用户签到状态查询相关的HTTP请求
 */
package org.example.serviceuser.user.signin.controller;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.signin.service.GetUserSignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/signin")
public class GetUserSignInController {
    
    private final GetUserSignInService getUserSignInService;
    
    @Autowired
    public GetUserSignInController(GetUserSignInService getUserSignInService) {
        this.getUserSignInService = getUserSignInService;
    }
    
    /**
     * 查询用户今日签到状态
     * 
     * @param userId 用户ID（从请求头获取）
     * @return ApiResponse 包含签到状态的响应
     */
    @GetMapping("status")
    public ApiResponse getSignInStatus(@RequestHeader(value = "X-User-Id") Long userId) {
        
        // 参数校验
        if (userId == null) {
            return ApiResponse.error(400, "用户ID不能为空");
        }
        
        // 查询签到状态
        boolean hasSignedIn = getUserSignInService.checkTodaySignInStatus(userId);
        
        return ApiResponse.ok("查询成功", hasSignedIn);
    }
}
