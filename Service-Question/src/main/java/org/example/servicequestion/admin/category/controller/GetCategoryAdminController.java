/**
 * File Name: GetCategoryAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-05
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.category.controller;

import jakarta.validation.Valid;
import org.example.servicequestion.admin.category.dto.CategoryListRequest;
import org.example.servicequestion.admin.category.service.GetCategoryAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/category")
public class GetCategoryAdminController {

    private final GetCategoryAdminService getCategoryAdminService;

    @Autowired
    public GetCategoryAdminController(GetCategoryAdminService getCategoryAdminService) {
        this.getCategoryAdminService = getCategoryAdminService;
    }

    /**
     * 获取分类列表
     *
     * @param request 请求参数
     * @return 分类列表和总数
     */
    @GetMapping("/list")
    public ApiResponse getCategoryList(@Valid CategoryListRequest request) {
        return getCategoryAdminService.getCategoryList(request);
    }

    /**
     * 获取项目选项列表
     *
     * @return 项目选项列表
     */
    @GetMapping("/project/options")
    public ApiResponse getProjectOptions() {
        return getCategoryAdminService.getProjectOptions();
    }
}
