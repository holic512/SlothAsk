/**
 * File Name: GetAdminController.java
 * Description: 用户管理系统-管理员查询用户控制器
 * Author: holic512
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage: 提供管理员查询用户列表的接口，支持分页、关键词搜索和状态筛选
 */
package org.example.serviceuser.admin.controller;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.admin.dto.PageDto;
import org.example.serviceuser.admin.service.GetUserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class GetUserAdminController {

    private final GetUserAdminService getUserAdminService;

    @Autowired
    public GetUserAdminController(GetUserAdminService getUserAdminService) {
        this.getUserAdminService = getUserAdminService;
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
        PageDto result = getUserAdminService.getUsers(keyword, status, pageNum, pageSize);

        return new ApiResponse(200, "查询成功", result);
    }
}
