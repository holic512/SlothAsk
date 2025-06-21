/**
 * File Name: PutUserInfoController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.info.controller;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.info.dto.UserProfileDTO;
import org.example.serviceuser.user.info.service.PutUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user/info")
public class PutUserInfoController {

    private final PutUserInfoService putUserInfoService;

    @Autowired
    public PutUserInfoController(PutUserInfoService putUserInfoService) {
        this.putUserInfoService = putUserInfoService;
    }

    @PutMapping("/avatar")
    public ApiResponse updateAvatar(@RequestHeader(value = "X-User-Id") Long userId,
                                    @RequestPart(value = "file") MultipartFile file) {

        //  调用服务类
        Pair<Boolean, String> result = putUserInfoService.updateAvatar(userId, file);

        if (result.getFirst()) {
            return new ApiResponse(200,"插入图片成功", result.getSecond());
        } else
            return new ApiResponse(400, result.getSecond());
    }
    
    /**
     * 更新用户个人资料
     * 
     * @param userId 用户ID
     * @param userProfileDTO 用户资料数据传输对象
     * @return API响应
     */
    @PutMapping("/UserProfile")
    public ApiResponse updateUserProfile(@RequestHeader(value = "X-User-Id") Long userId,
                                         @RequestBody UserProfileDTO userProfileDTO) {
        // 参数校验
        if (userId == null) {
            return new ApiResponse(400, "用户ID不能为空");
        }
        
        // 调用服务类更新用户资料
        boolean result = putUserInfoService.updateUserProfile(userId, userProfileDTO);
        
        if (result) {
            return new ApiResponse(200, "个人资料更新成功");
        } else {
            return new ApiResponse(400, "个人资料更新失败");
        }
    }
}
