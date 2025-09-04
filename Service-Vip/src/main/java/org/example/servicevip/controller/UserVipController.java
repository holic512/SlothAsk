/**
 * File Name: UserVipController.java
 * Description: 用户VIP信息控制器
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicevip.controller;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.servicevip.dto.VipInfoDTO;
import org.example.servicevip.service.UserVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vip")
public class UserVipController {

    @Autowired
    private UserVipService userVipService;

    /**
     * 根据用户ID查询VIP信息
     * @param userId 用户ID，从请求头获取
     * @return VIP信息（包含isActive状态）
     */
    @GetMapping("/user")
    public ApiResponse getUserVip(
            @RequestHeader(value = "X-User-Id") Long userId) {
        
        VipInfoDTO vipInfo = userVipService.getVipInfoByUserId(userId);
        
        if (vipInfo != null) {
            return ApiResponse.ok("查询成功", vipInfo);
        } else {
            return ApiResponse.error(404, "用户VIP信息不存在");
        }
    }

    /**
     * 检查用户VIP是否生效
     * @param userId 用户ID，从请求头获取
     * @return VIP是否生效
     */
    @GetMapping("/check")
    public ApiResponse checkVipStatus(
            @RequestHeader(value = "X-User-Id") Long userId) {
        
        boolean isVipActive = userVipService.isVipActive(userId);
        return ApiResponse.ok("检查完成", isVipActive);
    }

    /**
     * 检查用户VIP是否生效（专供Feign调用）
     * @param userId 用户ID，从请求头获取
     * @return VIP是否生效（直接返回Boolean）
     */
    @GetMapping("/check-feign")
    public Boolean checkVipStatusForFeign(
            @RequestHeader(value = "X-User-Id") Long userId) {
        
        return userVipService.isVipActive(userId);
    }
}