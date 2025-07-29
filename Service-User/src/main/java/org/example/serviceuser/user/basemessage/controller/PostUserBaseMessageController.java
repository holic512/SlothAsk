/**
 * File Name: PostUserBaseMessageController.java
 * Description: 用户基础消息POST操作控制器
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * 处理用户基础消息的POST请求操作
 */
package org.example.serviceuser.user.basemessage.controller;

import lombok.RequiredArgsConstructor;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.basemessage.service.PostUserBaseMessageService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("user/basemessage")
public class PostUserBaseMessageController {
    
    private final PostUserBaseMessageService postUserBaseMessageService;
    
    /**
     * 标记指定消息为已读
     * @param userId 用户ID，从请求头X-User-Id获取
     * @param messageId 消息ID
     * @return ApiResponse 包含操作结果的响应
     */
    @PostMapping("/mark-read/{messageId}")
    public ApiResponse markMessageAsRead(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable Long messageId) {
        
        if (userId == null) {
            return ApiResponse.error(400, "用户ID不能为空");
        }
        
        if (messageId == null) {
            return ApiResponse.error(400, "消息ID不能为空");
        }
        
        boolean success = postUserBaseMessageService.markMessageAsRead(userId, messageId);
        
        if (success) {
            return ApiResponse.ok("消息已标记为已读", null);
        } else {
            return ApiResponse.error(404, "消息不存在或无权限操作");
        }
    }
}