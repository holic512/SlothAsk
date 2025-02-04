/**
 * File Name: PutProjectAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.controller;

import jakarta.validation.Valid;
import org.example.servicequestion.admin.project.enums.PutProjectAdminEnum;
import org.example.servicequestion.admin.project.request.PutProjectAdminRequest;
import org.example.servicequestion.admin.project.service.PostProjectAdminService;
import org.example.servicequestion.admin.project.service.PutProjectAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/project")
public class PutProjectAdminController {
    private final PutProjectAdminService putProjectAdminService;

    @Autowired
    public PutProjectAdminController(PutProjectAdminService putProjectAdminService) {
        this.putProjectAdminService = putProjectAdminService;
    }

    /**
     * 编辑项目接口
     * @param request 请求对象，包含要编辑的项目信息
     * @return ApiResponse 统一响应对象
     */
    @PutMapping("/admin/project/edit")
    public ApiResponse editProject(@Valid @RequestBody PutProjectAdminRequest request) {
        PutProjectAdminEnum result = putProjectAdminService.modifyProject(request);

        switch (result) {
            case SUCCESS:
                return new ApiResponse(200, result.getValue());
            case PROJECT_NOT_FOUND:
                return new ApiResponse(404, result.getValue());
            case UPDATE_FAILED:
                return new ApiResponse(400, result.getValue());
            case SYSTEM_ERROR:
                return new ApiResponse(500, result.getValue());
            default:
                return new ApiResponse(500, "未知错误");
        }
    }
}
