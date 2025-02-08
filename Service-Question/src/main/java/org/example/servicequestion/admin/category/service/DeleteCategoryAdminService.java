package org.example.servicequestion.admin.category.service;

import java.util.List;

public interface DeleteCategoryAdminService {
    /**
     * 删除单个分类
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 批量删除分类
     * @param ids 分类ID列表
     */
    void deleteBatchCategories(List<Long> ids);
} 