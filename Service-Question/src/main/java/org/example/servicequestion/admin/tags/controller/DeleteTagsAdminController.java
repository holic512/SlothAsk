/**
 * File Name: DeleteTagsAdminController.java
 * Description: 标签管理系统的删除控制器，提供标签的单个删除和批量删除功能
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   - DELETE /admin/tags/delete/{id} 删除单个标签
 *   - DELETE /admin/tags/batchDelete 批量删除标签
 * 
 * 该控制器负责处理与标签删除相关的所有HTTP请求，包括：
 * 1. 单个标签的删除操作
 * 2. 多个标签的批量删除操作
 * 所有操作都会返回统一的ApiResponse响应格式
 */
package org.example.servicequestion.admin.tags.controller;

import java.util.List;

import org.example.servicequestion.admin.tags.service.DeleteTagsAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tags")
public class DeleteTagsAdminController {

    private final DeleteTagsAdminService deleteTagsAdminService;

    @Autowired
    public DeleteTagsAdminController(DeleteTagsAdminService deleteTagsAdminService) {
        this.deleteTagsAdminService = deleteTagsAdminService;
    }

    /**
     * 删除单个标签
     * @param id 标签ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteTag(@PathVariable Long id) {
        boolean success = deleteTagsAdminService.deleteTag(id);
        return success ? 
            new ApiResponse(200, "删除成功", null) : 
            new ApiResponse(500, "删除失败", null);
    }

    /**
     * 批量删除标签
     * @param ids 标签ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batchDelete")
    public ApiResponse batchDeleteTags(@RequestBody List<Long> ids) {
        boolean success = deleteTagsAdminService.batchDeleteTags(ids);
        return success ? 
            new ApiResponse(200, "批量删除成功", null) : 
            new ApiResponse(500, "批量删除失败", null);
    }
}
