/**
 * File Name: GetAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.controller;

import org.example.serviceuser.admin.dto.PageDto;
import org.example.serviceuser.admin.service.GetUserService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class GetUserController {

    private final GetUserService getUserService;

    @Autowired
    public GetUserController(GetUserService getUserService) {
        this.getUserService = getUserService;
    }


    /**
     * 获取用户列表
     *
     * @param keyword  搜索关键词（用户名、邮箱、手机号）
     * @param status   用户状态，1 为正常，0 为禁用
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     * @return 返回用户列表的结果 和 用户总数
     */
    @GetMapping("/list")
    public ApiResponse getUsers(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Integer status,
                                @RequestParam int pageNum,
                                @RequestParam int pageSize) {

        // 调用 Service 层方法，获取用户列表
        PageDto result = getUserService.getUsers(keyword, status, pageNum, pageSize);

        return new ApiResponse(200, "查询成功", result);
    }
}
