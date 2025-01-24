/**
 * File Name: DeleteAdminController.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.controller;

import org.example.serviceuser.admin.request.BatchDeleteRequest;
import org.example.serviceuser.admin.service.DeleteUserService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class DeleteUserController {

    private final DeleteUserService deleteUserService;

    @Autowired
    public DeleteUserController(DeleteUserService deleteUserService) {
        this.deleteUserService = deleteUserService;
    }


    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteUser(@PathVariable Long id) {
        int result = deleteUserService.deleteUser(id);

        // 判断删除是否成功
        if (result > 0) {
            return new ApiResponse(200, "success", null);
        } else {
            return new ApiResponse(500, "failed", null);
        }
    }

    @DeleteMapping("/delete/batch")
    public ApiResponse deleteUsersBatch(@RequestBody BatchDeleteRequest request) {
        // 获取批量删除的用户 ID 列表
        List<Long> ids = request.getIds();

        // 调用 service 层进行批量删除
        boolean success = deleteUserService.deleteUsersBatch(ids);

        // 判断删除是否成功
        if (success) {
            return new ApiResponse(200, "success", null);
        } else {
            return new ApiResponse(500, "failed", null);
        }
    }
}
