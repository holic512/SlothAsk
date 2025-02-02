/**
 * File Name: PostProjectAdminController.java
 * Description: 项目管理控制器
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 */
package org.example.servicequestion.admin.project.controller;

import jakarta.validation.Valid;
import org.example.servicequestion.admin.project.enums.PostProjectAdminEnum;
import org.example.servicequestion.admin.project.request.AddProjectAdminRequest;
import org.example.servicequestion.admin.project.service.PostProjectAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/project")
public class PostProjectAdminController {

    private final PostProjectAdminService postProjectAdminService;

    @Autowired
    public PostProjectAdminController(PostProjectAdminService postProjectAdminService) {
        this.postProjectAdminService = postProjectAdminService;
    }

    /**
     * 添加新项目接口
     *
     * @param request 添加项目请求对象
     * @return ApiResponse 统一响应对象
     */
    @PostMapping("/add")
    public ApiResponse addProject(@Valid @RequestBody AddProjectAdminRequest request) {
        PostProjectAdminEnum result = postProjectAdminService.addProject(request);

        switch (result) {
            case SUCCESS:
                return new ApiResponse(200, result.getValue());
            case SAVE_FAILED:
                return new ApiResponse(400, result.getValue());
            case SYSTEM_ERROR:
                return new ApiResponse(500, result.getValue());
            default:
                return new ApiResponse(500, "未知错误");
        }
    }
}
