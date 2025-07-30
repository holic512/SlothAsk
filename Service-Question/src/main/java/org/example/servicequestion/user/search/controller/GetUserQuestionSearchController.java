/**
 * File Name: GetUserQuestionSearchController.java
 * Description: 用户题目搜索控制器
 * Author: holic512
 * Created Date: 2025-07-30
 * Version: 1.0
 * Usage:
 * 提供题目向量化和搜索相关的API接口
 */
package org.example.servicequestion.user.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.servicequestion.config.Qdrant.QdrantVectorService;
import org.example.servicequestion.user.commonService.IdConversionService;
import org.example.servicequestion.user.search.service.QuestionVectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户题目搜索控制器
 * 提供题目向量化和搜索相关的RESTful API接口
 */
@Slf4j
@RestController
@RequestMapping("/user/search")
public class GetUserQuestionSearchController {

    @Autowired
    private QuestionVectorService questionVectorService;

    @Autowired
    private IdConversionService idConversionService;

    /**
     * 手动批量全部向量化
     * 异步处理，覆盖式更新，有锁防止重复执行
     *
     * @param userId 用户ID,从请求头X-User-Id获取,可选
     * @param upcId  项目ID,从请求头X-Upc-Id获取,可选
     * @return 包含操作结果的API响应
     */
    @PostMapping("/vectorize/batch")
    public ApiResponse manualBatchVectorizeAllQuestions(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {

        boolean success = questionVectorService.manualBatchVectorizeAllQuestions();

        if (success) {
            return ApiResponse.ok("向量化任务已启动，正在后台处理中");
        } else {
            return ApiResponse.error(409, "向量化任务正在进行中，请稍后再试");
        }
    }

    /**
     * 获取向量化进度
     *
     * @param userId 用户ID,从请求头X-User-Id获取,可选
     * @param upcId  项目ID,从请求头X-Upc-Id获取,可选
     * @return 包含进度信息的API响应
     */
    @GetMapping("/vectorize/progress")
    public ApiResponse getVectorizationProgress(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {

        String progress = questionVectorService.getVectorizationProgress();
        return ApiResponse.ok("获取向量化进度成功", progress);
    }

    /**
     * 相似度查询接口
     * 根据输入文本查询相似的题目
     *
     * @param queryText 查询文本
     * @return 相似题目列表（ID已转换为虚拟ID）
     */
    @PostMapping("/similarity-search")
    public ApiResponse searchSimilarQuestions(@RequestParam String queryText) {
        try {
            List<QdrantVectorService.SimilarityResult> results = questionVectorService.searchSimilarQuestions(queryText);
            
            // 将真实ID转换为虚拟ID
            List<QdrantVectorService.SimilarityResult> convertedResults = results.stream()
                    .map(result -> {
                        try {
                            Long originalId = Long.parseLong(result.getId());
                            String virtualId = idConversionService.getVirtualIdFromOriginalId(originalId);
                            return new QdrantVectorService.SimilarityResult(virtualId, result.getTitle(), result.getScore());
                        } catch (NumberFormatException e) {
                            log.warn("无法解析ID: {}", result.getId());
                            return result; // 如果ID格式不正确，返回原始结果
                        }
                    })
                    .collect(Collectors.toList());
            
            return ApiResponse.ok(convertedResults);
        } catch (Exception e) {
            log.error("相似度查询失败", e);
            return ApiResponse.error(400, "相似度查询失败: " + e.getMessage());
        }
    }
}
