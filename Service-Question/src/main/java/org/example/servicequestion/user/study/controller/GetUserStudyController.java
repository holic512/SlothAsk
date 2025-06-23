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

import jakarta.validation.Valid;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.entity.QuestionCategory;
import org.example.servicequestion.user.study.dto.CategoryIdAndNameDto;
import org.example.servicequestion.user.study.dto.UserSubmitCountDto;
import org.example.servicequestion.user.study.request.GetQuestionListRequest;
import org.example.servicequestion.user.study.service.GetUserStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 支持按分类、标签、类型、难度等条件过滤，支持等于/不等于操作
     * 支持条件匹配模式：全部满足(AND)或任一满足(OR)
     * 每页返回20条数据
     *
     * @param userId  用户ID,从请求头X-User-Id获取,可选
     * @param upcId   项目ID,从请求头X-Upc-Id获取,可选
     * @param request 包含过滤条件的请求对象，支持以下筛选条件：
     *                - matchAllConditions: 条件匹配模式，true=全部满足(AND)，false=任一满足(OR)
     *                - filterCategoryEquals/NotEquals: 分类等于/不等于筛选
     *                - filterTypeEquals/NotEquals: 类型等于/不等于筛选
     *                - filterDifficultyEquals/NotEquals: 难度等于/不等于筛选
     *                - filterTags: 标签筛选列表
     *                - searchText: 关键词搜索
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

    /**
     * 根据当前题目的虚拟ID，获取同一分类下的下一题虚拟ID
     *
     * @param currentVid 当前题目的虚拟ID
     * @return 包含下一题虚拟ID的API响应
     */
    @GetMapping("/nextQuestion/{currentVid}")
    public ApiResponse getNextQuestionVid(
            @PathVariable String currentVid
    ) {
        String nextVid = getUserStudyService.getNextQuestionVid(currentVid);
        return new ApiResponse(200, "获取下一题虚拟ID成功", nextVid);
    }

    /**
     * 获取用户提交次数统计
     * 查询Redis中当天的提交次数缓存数据，同时从数据库中查询最近119天的提交记录
     *
     * @param userId 用户ID,从请求头X-User-Id获取
     * @return 包含用户提交次数统计的API响应
     */
    @GetMapping("/submitCountStats")
    public ApiResponse getUserSubmitCountStats(
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<UserSubmitCountDto> stats = getUserStudyService.getUserSubmitCountStats(userId);
        return new ApiResponse(200, "获取用户提交次数统计成功", stats);
    }
}
