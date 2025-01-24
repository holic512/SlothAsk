/**
 * File Name: DeleteAdminController.java
 * Description: 用户管理系统-管理员删除用户控制器
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 提供管理员删除用户的相关接口，包括单个删除和批量删除功能
 */
package org.example.serviceuser.admin.controller;

import jakarta.validation.constraints.Min;
import org.example.serviceuser.admin.request.BatchDeleteRequest;
import org.example.serviceuser.admin.service.DeleteUserAdminService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class DeleteUserAdminController {

    private final DeleteUserAdminService deleteUserAdminService;

    @Autowired
    public DeleteUserAdminController(DeleteUserAdminService deleteUserAdminService) {
        this.deleteUserAdminService = deleteUserAdminService;
    }

    /**
     * 删除单个用户
     * @param id 要删除的用户ID
     * @return ApiResponse 删除操作的响应结果
     * 成功返回: 200 状态码和成功消息
     * 失败返回: 400 状态码和失败消息
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteUser(@PathVariable @Min(value = 1, message = "id不能为空") Long id) {
        int result = deleteUserAdminService.deleteUser(id);

        // 判断删除是否成功
        if (result > 0) {
            return new ApiResponse(200, "删除用户成功");
        } else {
            return new ApiResponse(400, "删除用户失败", null);
        }
    }

    /**
     * 批量删除用户
     * @param request 包含要删除的用户ID列表的请求对象
     * @return ApiResponse 批量删除操作的响应结果
     * 成功返回: 200 状态码和成功消息
     * 失败返回: 400 状态码和失败消息
     */
    @DeleteMapping("/delete/batchUsers")
    public ApiResponse deleteUsersBatch(@RequestBody BatchDeleteRequest request) {
        // 获取批量删除的用户 ID 列表
        List<Long> ids = request.getIds();

        // 调用 service 层进行批量删除
        boolean success = deleteUserAdminService.deleteUsersBatch(ids);

        // 判断删除是否成功
        if (success) {
            return new ApiResponse(200, "删除用户成功");
        } else {
            return new ApiResponse(400, "删除用户失败");
        }
    }
}
