/**
 * File Name: GetUserCategoryController.java
 * Description: 用户题库分类控制器
 * Author: holic512
 * Created Date: 2025-03-13
 * Version: 1.0
 * Usage:
 * 处理用户题库分类相关的请求
 */
package org.example.servicequestion.user.category.controller;

import java.util.List;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.user.category.service.GetUserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/category")
public class GetUserCategoryController {
    
    @Autowired
    private GetUserCategoryService getUserCategoryService;
    
    /**
     * 获取推荐的题库分类
     * 不存在X-Upc-Id时: 从全部分类中获取六个分类，优先级为：排序、访问量、id
     * 存在X-Upc-Id时: 限定条件为project_id等于X-Upc-Id，再按优先级获取分类
     * 
     * @param userId 用户ID
     * @param upcId 项目ID，可能为null
     * @return 推荐的题库分类列表
     */
    @GetMapping("/recommend")
    public ApiResponse getRecommendedCategories(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {
        
        List<QuestionCategory> categories = getUserCategoryService.getRecommendedCategories(upcId);
        return new ApiResponse(200, "获取推荐题库分类成功", categories);
    }
    
    @GetMapping("")
    public ApiResponse test(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {
        return null;
    }
}
