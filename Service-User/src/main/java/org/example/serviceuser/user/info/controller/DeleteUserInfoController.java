/**
 * File Name: DeleteUserInfoController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.info.controller;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.info.service.DeleteUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/info")
public class DeleteUserInfoController {

    private final DeleteUserInfoService deleteUserInfoService;

    @Autowired
    public DeleteUserInfoController(DeleteUserInfoService deleteUserInfoService) {
        this.deleteUserInfoService = deleteUserInfoService;
    }

    @DeleteMapping("/avatar")
    public ApiResponse deleteAvatar(@RequestHeader(value = "X-User-Id") Long userId) {
        if (userId == null) {
            return new ApiResponse(404, "用户id为空");
        }
        deleteUserInfoService.deleteAvatar(userId);
        return new ApiResponse(200, "删除头像成功");
    }
}
