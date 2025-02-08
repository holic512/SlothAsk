package org.example.servicequestion.admin.category.service;

import org.example.servicequestion.admin.category.dto.CategoryUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PutCategoryAdminService {
    /**
     * 更新分类
     *
     * @param request 更新分类请求
     * @param file    更新图片请求（可选）
     */
    void updateCategory(CategoryUpdateRequest request, MultipartFile file);
} 