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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.example.servicequestion.admin.project.enums.GetProjectAdminEnum;
import org.example.servicequestion.admin.project.request.GetProjectAdminRequest;
import org.example.servicequestion.admin.project.service.GetProjectAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.entity.ProjectCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/project")
public class GetProjectAdminController {

    private final GetProjectAdminService getProjectAdminService;

    @Autowired
    public GetProjectAdminController(GetProjectAdminService getProjectAdminService) {
        this.getProjectAdminService = getProjectAdminService;
    }

    @GetMapping("/list")
    public ApiResponse getProjectList(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) Integer sortType) {
        GetProjectAdminRequest request = new GetProjectAdminRequest();
        request.setSearch(search);
        request.setSortType(sortType);

        // 用于接收查询结果
        List<ProjectCategory> projectList = new ArrayList<>();

        // 调用 Service 获取查询状态
        GetProjectAdminEnum result = getProjectAdminService.getProjectList(request, projectList);

        switch (result) {
            case SUCCESS:
                // 查询成功，返回查询到的项目列表
                return new ApiResponse(200, "查询成功", projectList.isEmpty() ? null : projectList);
            case SEARCH_FAILED:
                // 查询失败，暂无数据
                return new ApiResponse(200, "暂无数据", result.getValue());
            case SYSTEM_ERROR:
                // 系统错误
                return new ApiResponse(500, "系统错误", result.getValue());
            default:
                return new ApiResponse(500, "未知错误");
        }
    }


}
