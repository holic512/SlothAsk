/**
 * File Name: UpdateTagsAdminController.java
 * Description: 标签管理系统的更新控制器，提供标签信息修改功能
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   - PUT /admin/tags/update 更新标签信息
 * 
 * 该控制器负责处理与标签更新相关的所有HTTP请求：
 * 1. 更新标签的基本信息（名称、描述、排序等）
 * 2. 支持修改标签所属项目
 * 3. 返回统一的ApiResponse响应格式
 */
package org.example.servicequestion.admin.tags.controller;

import org.example.servicequestion.admin.tags.dto.UpdateTagDto;
import org.example.servicequestion.admin.tags.service.UpdateTagsAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tags")
public class UpdateTagsAdminController {

    private final UpdateTagsAdminService updateTagsAdminService;

    @Autowired
    public UpdateTagsAdminController(UpdateTagsAdminService updateTagsAdminService) {
        this.updateTagsAdminService = updateTagsAdminService;
    }

    /**
     * 更新标签信息
     * @param updateTagDto 更新的标签信息
     * @return 操作结果
     */
    @PutMapping("/update")
    public ApiResponse updateTag(@RequestBody UpdateTagDto updateTagDto) {
        boolean success = updateTagsAdminService.updateTag(updateTagDto);
        return success ? 
            new ApiResponse(200, "更新成功", null) : 
            new ApiResponse(500, "更新失败", null);
    }
} 