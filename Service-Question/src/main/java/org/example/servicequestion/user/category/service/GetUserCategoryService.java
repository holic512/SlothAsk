/**
 * File Name: GetUserCategoryService.java
 * Description: 获取用户题库分类的服务接口
 * Author: holic512
 * Created Date: 2025-03-19
 * Version: 1.0
 * Usage:
 * 提供用户题库分类相关的查询服务
 */
package org.example.servicequestion.user.category.service;

import java.util.List;

import org.example.servicequestion.entity.QuestionCategory;

public interface GetUserCategoryService {
    /**
     * 获取推荐的题库分类
     * @param projectId 项目ID，可能为null
     * @return 推荐的题库分类列表
     */
    List<QuestionCategory> getRecommendedCategories(Long projectId);
}
