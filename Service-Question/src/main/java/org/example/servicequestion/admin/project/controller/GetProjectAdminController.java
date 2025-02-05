/**
 * File Name: GetProjectAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.controller;

import org.example.servicequestion.admin.project.enums.GetProjectAdminEnum;
import org.example.servicequestion.admin.project.request.GetProjectAdminRequest;
import org.example.servicequestion.admin.project.response.GetProjectAdminResponse;
import org.example.servicequestion.admin.project.service.GetProjectAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/project")
public class GetProjectAdminController {

    private final GetProjectAdminService getProjectAdminService;

    @Autowired
    public GetProjectAdminController(GetProjectAdminService getProjectAdminService) {
        this.getProjectAdminService = getProjectAdminService;
    }

    @GetMapping("/list")
    public ApiResponse getProjectList(@Valid GetProjectAdminRequest request) {

        GetProjectAdminResponse response = new GetProjectAdminResponse();
        GetProjectAdminEnum result = getProjectAdminService.getProjectList(request, response);

        switch (result) {
            case SUCCESS:
                return new ApiResponse(200, "查询成功", response);
            case SEARCH_FAILED:
                return new ApiResponse(200, "暂无数据", null);
            case SYSTEM_ERROR:
                return new ApiResponse(500, "系统错误", null);
            default:
                return new ApiResponse(500, "未知错误", null);
        }
    }


}
