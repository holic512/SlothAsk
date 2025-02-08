package org.example.servicequestion.admin.category.controller;

import org.example.servicequestion.admin.category.dto.CategoryUpdateRequest;
import org.example.servicequestion.admin.category.service.PutCategoryAdminService;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/category")
@Validated
public class PutCategoryAdminController {

    @Autowired
    private PutCategoryAdminService putCategoryAdminService;

    /**
     * 更新分类
     */
    @PutMapping("/update")
    public ApiResponse updateCategory(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("metadata") @Valid CategoryUpdateRequest request
    ) {
        putCategoryAdminService.updateCategory(request, file);
        return new ApiResponse(200, "更新成功");
    }
} 