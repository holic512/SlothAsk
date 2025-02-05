/**
 * File Name: DeleteProjectAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.controller;

import org.example.servicequestion.admin.project.enums.DeleteProjectAdminEnum;
import org.example.servicequestion.admin.project.enums.DeleteProjectsAdminEnum;
import org.example.servicequestion.admin.project.service.DeleteProjectAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.servicequestion.admin.project.enums.DeleteProjectsAdminEnum.NO_IDS_PROVIDED;
import static org.example.servicequestion.admin.project.enums.GetProjectAdminEnum.SUCCESS;

@RestController
@RequestMapping("/admin/project")
public class DeleteProjectAdminController {
    private final DeleteProjectAdminService deleteProjectAdminService;

    @Autowired
    public DeleteProjectAdminController(DeleteProjectAdminService deleteProjectAdminService) {
        this.deleteProjectAdminService = deleteProjectAdminService;
    }

    /**
     * 删除项目接口
     *
     * @param id 项目ID
     * @return ApiResponse 统一响应对象
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteProject(@PathVariable Long id) {
        DeleteProjectAdminEnum result = deleteProjectAdminService.deleteProject(id);

        return switch (result) {
            case SUCCESS -> new ApiResponse(200, result.getValue());
            case PROJECT_NOT_FOUND -> new ApiResponse(404, result.getValue());
            case DELETE_FAILED -> new ApiResponse(400, result.getValue());
            case SYSTEM_ERROR -> new ApiResponse(500, result.getValue());
            default -> new ApiResponse(500, "未知错误");
        };
    }

    /**
     * 批量删除项目接口
     * @param ids 项目ID列表
     * @return ApiResponse 统一响应对象
     */
    @DeleteMapping("/delete")
    public ApiResponse deleteProjects(@RequestBody List<Long> ids) {
        DeleteProjectsAdminEnum result = deleteProjectAdminService.deleteProjects(ids);

        switch (result) {
            case SUCCESS:
                return new ApiResponse(200, result.getValue());
            case NO_IDS_PROVIDED:
                return new ApiResponse(400, result.getValue());
            case DELETE_FAILED:
                return new ApiResponse(400, result.getValue());
            case SYSTEM_ERROR:
                return new ApiResponse(500, result.getValue());
            default:
                return new ApiResponse(500, "未知错误");
        }
    }
}
