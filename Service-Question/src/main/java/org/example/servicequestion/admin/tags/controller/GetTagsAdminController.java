/**
 * File Name: GetTagsAdminController.java
 * Description: 标签管理系统的查询控制器，提供标签列表和项目列表的查询功能
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage: 
 *   - GET /admin/tags/getProjectList 获取项目列表
 *   - GET /admin/tags/list 获取标签列表（支持分页和关键字搜索）
 * 
 * 该控制器负责处理与标签查询相关的所有HTTP请求，包括：
 * 1. 获取所有项目列表
 * 2. 分页查询标签列表，支持按关键字搜索
 * 所有接口都返回统一的ApiResponse响应格式
 */
package org.example.servicequestion.admin.tags.controller;

import org.example.servicequestion.admin.tags.service.GetTagsAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tags")
public class GetTagsAdminController {

    private final GetTagsAdminService getTagsAdminService;

    @Autowired
    public GetTagsAdminController(GetTagsAdminService getTagsAdminService) {
        this.getTagsAdminService = getTagsAdminService;
    }

    @GetMapping("/getProjectList")
    public ApiResponse getProjectList() {
        return new ApiResponse(200, "获取项目列表成功", getTagsAdminService.getProjectList());
    }

    /**
     * 分页查询标签列表
     * @param keyword 搜索关键字（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public ApiResponse getTagsList(
            @RequestParam(required = false) String keyword,
            @RequestParam int pageNum,
            @RequestParam int pageSize) {
        return new ApiResponse(200, "获取标签列表成功", getTagsAdminService.getTagsList(keyword, pageNum, pageSize));
    }

}
