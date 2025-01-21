/**
 * File Name: testController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-06
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.controller;

import org.example.serviceadmin.config.ApiResponse.ApiResponse;
import org.example.serviceuser.dto.ResultDto;
import org.example.serviceuser.dto.UserDto;
import org.example.serviceuser.dto.UserProfileDto;
import org.example.serviceuser.entity.BatchDeleteRequest;
import org.example.serviceuser.entity.UserUpdateRequest;
import org.example.serviceuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserService UserController(UserService userService) {
        return this.userService = userService;
    }

    @PutMapping("/password")
    public ResultDto UpdatePassword(@RequestParam("id")int id, @RequestParam("password")String password) {
        String newPassword = userService.UpdatePassword(id, password);

        if("failure".equals(newPassword)) {
            return new ResultDto(500,"failure",null);
        }

        return new ResultDto(200,"success",null);

    }

    /**
     * 获取用户列表
     *
     * @param keyword  搜索关键词（用户名、邮箱、手机号）
     * @param status   用户状态，1 为正常，0 为禁用
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     * @return 返回用户列表的结果
     */
    @GetMapping("/list")
    public ResultDto getUsers(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Integer status,
                              @RequestParam int pageNum,
                              @RequestParam int pageSize) {
        // 调用 Service 层方法，获取用户列表
        return userService.getUsers(keyword, status, pageNum, pageSize);
    }

    @PostMapping("/add")
    public ResultDto addUser(@RequestBody UserDto userDto) {

    }

    @PutMapping("/update")
    public ResultDto updateUser(@RequestBody UserUpdateRequest request) {
        UserDto userDto = request.getUserDto();
        UserProfileDto userProfileDto = request.getUserProfileDto();

        // 如果 userDto 为空，返回错误响应
        if (userDto == null) {
            return new ResultDto(400, "data is null", null);
        }

        // 调用 service 层进行更新
        int userUpdateResult = userService.updateUser(userDto);
        int profileUpdateResult = userService.updateUserProfile(userProfileDto);

        if (userUpdateResult > 0 && profileUpdateResult > 0) {
            return new ResultDto(200, "success", null);
        } else {
            return new ResultDto(500, "failed", null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResultDto deleteUser(@PathVariable int id) {
        int result = userService.deleteUser(id);

        // 判断删除是否成功
        if (result > 0) {
            return new ResultDto(200, "success", null);
        } else {
            return new ResultDto(500, "failed", null);
        }
    }

    @DeleteMapping("/delete/batch")
    public ResultDto deleteUsersBatch(@RequestBody BatchDeleteRequest request) {
        // 获取批量删除的用户 ID 列表
        List<Integer> ids = request.getIds();

        // 调用 service 层进行批量删除
        int result = userService.deleteUsersBatch(ids);

        // 判断删除是否成功
        if (result > 0) {
            return new ResultDto(200, "success", null);
        } else {
            return new ResultDto(500, "failed", null);
        }
    }

}

