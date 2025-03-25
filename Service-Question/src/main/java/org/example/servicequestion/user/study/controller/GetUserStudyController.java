/**
 * File Name: GetUserCategoryController.java
 * Description: study 首页 分类控制器
 * Author: holic512
 * Created Date: 2025-03-13
 * Version: 1.0
 * Usage:
 * 处理 study 首页的 相关的HTTP请求,包括:
 * - 获取推荐分类
 * - 获取分类列表
 * - 获取标签列表
 * - 获取题目列表
 */
package org.example.servicequestion.user.study.controller;

import java.util.List;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.user.study.dto.CategoryIdAndNameDto;
import org.example.servicequestion.user.study.request.GetQuestionListRequest;
import org.example.servicequestion.user.study.service.GetUserStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

/**
 * 用户题库分类控制器
 * 提供题库分类相关的RESTful API接口
 */
@RestController
@RequestMapping("/user/study")
public class GetUserStudyController {

    @Autowired
    private GetUserStudyService getUserStudyService;

    /**
     * 获取推荐的题库分类
     * 不存在X-Upc-Id时: 从全部分类中获取六个分类，优先级为：排序、访问量、id
     * 存在X-Upc-Id时: 限定条件为project_id等于X-Upc-Id，再按优先级获取分类
     *
     * @param userId 用户ID,从请求头X-User-Id获取,可选
     * @param upcId  项目ID,从请求头X-Upc-Id获取,可选
     * @return 包含推荐分类列表的API响应
     */
    @GetMapping("/recommend")
    public ApiResponse getRecommendedCategories(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {

        List<QuestionCategory> categories = getUserStudyService.getRecommendedCategories(upcId);
        return new ApiResponse(200, "获取推荐题库分类成功", categories);
    }

    /**
     * 获取项目下所有分类的ID和名称
     * 
     * @param userId 用户ID,从请求头X-User-Id获取,可选
     * @param upcId  项目ID,从请求头X-Upc-Id获取,可选
     * @return 包含分类ID和名称列表的API响应
     */
    @GetMapping("/categoryIdAndName")
    public ApiResponse getCategoryIdAndName(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {

        List<CategoryIdAndNameDto> categories = getUserStudyService.getCategoryIdAndName(upcId);
        return new ApiResponse(200, "获取全部题库分类成功", categories);
    }

    /**
     * 获取项目下所有标签的ID和名称
     *
     * @param userId 用户ID,从请求头X-User-Id获取,可选
     * @param upcId  项目ID,从请求头X-Upc-Id获取,可选
     * @return 包含标签ID和名称列表的API响应
     */
    @GetMapping("/tagIdAndName")
    public ApiResponse getTagIdAndName(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId
    ) {
        return new ApiResponse(200, "获取对应标签成功",
                getUserStudyService.getTagIdAndName(upcId));
    }

    /**
     * 获取项目下符合过滤条件的题目列表
     * 支持按分类、标签、类型、难度等条件过滤
     * 每页返回20条数据
     *
     * @param userId  用户ID,从请求头X-User-Id获取,可选
     * @param upcId   项目ID,从请求头X-Upc-Id获取,可选
     * @param request 包含过滤条件的请求对象
     * @return 包含分页题目列表的API响应
     */
    @GetMapping("/questionList")
    public ApiResponse getQuestionList(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId,
            @Valid GetQuestionListRequest request
    ) {

        return new ApiResponse(200, "获取对应问题列表成功",
                getUserStudyService.getQuestionList(upcId, request));
    }


}
