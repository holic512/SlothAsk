package org.example.servicequestion.admin.category.service;

import org.example.servicequestion.admin.category.dto.CategoryListRequest;
import org.example.servicequestion.config.ApiResponse.ApiResponse;

/**
 * 分类管理服务接口
 */
public interface GetCategoryAdminService {

    /**
     * 获取分类列表
     *
     * @param request 请求参数
     * @return ApiResponse 包含分类列表和总数的响应
     */
    ApiResponse getCategoryList(CategoryListRequest request);


    /**
     * 获取项目选项列表
     *
     * @return ApiResponse 包含项目选项列表的响应
     */
    ApiResponse getProjectOptions();
}