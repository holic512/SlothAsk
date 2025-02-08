/**
 * File Name: PostCategoryAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-06
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.category.service;

import org.example.servicequestion.admin.category.dto.CategoryAddRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PostCategoryAdminService {
    /**
     * 添加分类
     *
     * @param request 添加分类请求
     * @param file    添加图片请求
     * @return 新增分类的ID
     */
    Long addCategory(CategoryAddRequest request, MultipartFile file);
}
