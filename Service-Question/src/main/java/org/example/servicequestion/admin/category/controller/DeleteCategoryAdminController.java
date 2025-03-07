/**
 * File Name: DeleteCategoryAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-08
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.category.controller;


import java.util.List;

import org.example.servicequestion.admin.category.service.DeleteCategoryAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/category")
public class DeleteCategoryAdminController {
    
    @Autowired
    private DeleteCategoryAdminService deleteCategoryAdminService;

    /**
     * 删除单个分类
     */
    @DeleteMapping("/{id}")
    public ApiResponse deleteCategory(@PathVariable Long id) {
        deleteCategoryAdminService.deleteCategory(id);
        return new ApiResponse(200, "删除成功");
    }

    /**
     * 批量删除分类
     */
    @DeleteMapping("/batch")
    public ApiResponse deleteBatchCategories(@RequestBody List<Long> ids) {
        deleteCategoryAdminService.deleteBatchCategories(ids);
        return new ApiResponse(200, "批量删除成功");
    }
}
