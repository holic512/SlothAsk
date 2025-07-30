/**
 * File Name: QdrantTestController.java
 * Description: Qdrant向量服务测试控制器
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * 提供REST API接口用于测试Qdrant向量服务的各项功能
 */
package org.example.servicequestion.config.Qdrant;

import lombok.Data;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/qdrant/test")
public class QdrantTestController {

    @Autowired
    private QdrantVectorService qdrantVectorService;

    /**
     * 插入单个向量
     */
    @PostMapping("/insert")
    public ApiResponse insertVector(@RequestBody InsertVectorRequest request) {
        try {
            boolean success = qdrantVectorService.insertVector(
                    request.getId() != null ? request.getId() : UUID.randomUUID().toString(),
                    request.getTitle(),
                    request.getVector()
            );
            return success ? ApiResponse.ok("向量插入成功", true) : ApiResponse.error(500, "向量插入失败");
        } catch (Exception e) {
            return ApiResponse.error(500, "插入向量时发生错误: " + e.getMessage());
        }
    }

    /**
     * 批量插入向量
     */
    @PostMapping("/batch-insert")
    public ApiResponse batchInsertVectors(@RequestBody BatchInsertRequest request) {
        try {
            List<QdrantVectorService.VectorData> vectorDataList = request.getVectorDataList().stream()
                    .map(data -> new QdrantVectorService.VectorData(
                            data.getId() != null ? data.getId() : UUID.randomUUID().toString(),
                            data.getTitle(),
                            data.getVector()
                    ))
                    .toList();

            int insertedCount = qdrantVectorService.batchInsertVectors(vectorDataList);
            return ApiResponse.ok("成功插入 " + insertedCount + " 个向量", insertedCount);
        } catch (Exception e) {
            return ApiResponse.error(500, "批量插入向量时发生错误: " + e.getMessage());
        }
    }

    /**
     * 相似度查询
     */
    @PostMapping("/search")
    public ApiResponse searchSimilarVectors(
            @RequestBody SearchVectorRequest request) {
        try {
            List<QdrantVectorService.SimilarityResult> results;

            if (request.getThreshold() != null && request.getLimit() != null) {
                results = qdrantVectorService.searchSimilarVectors(
                        request.getQueryVector(),
                        request.getThreshold(),
                        request.getLimit()
                );
            } else {
                results = qdrantVectorService.searchSimilarVectors(request.getQueryVector());
            }

            return ApiResponse.ok("查询到 " + results.size() + " 个相似向量", results);
        } catch (Exception e) {
            return ApiResponse.error(500, "相似度查询时发生错误: " + e.getMessage());
        }
    }

    /**
     * 删除向量
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteVector(@PathVariable String id) {
        try {
            boolean success = qdrantVectorService.deleteVector(id);
            return success ? ApiResponse.ok("向量删除成功", true) : ApiResponse.error(500, "向量删除失败");
        } catch (Exception e) {
            return ApiResponse.error(500, "删除向量时发生错误: " + e.getMessage());
        }
    }


    /**
     * 获取当前配置
     */
    @GetMapping("/config")
    public ApiResponse getConfig() {
        try {
            ConfigInfo config = new ConfigInfo(
                    qdrantVectorService.getSimilarityThreshold(),
                    qdrantVectorService.getQueryLimit()
            );
            return ApiResponse.ok("获取配置信息成功", config);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取配置信息时发生错误: " + e.getMessage());
        }
    }

    /**
     * 更新配置
     */
    @PostMapping("/config")
    public ApiResponse updateConfig(@RequestBody ConfigInfo config) {
        try {
            if (config.getSimilarityThreshold() != null) {
                qdrantVectorService.setSimilarityThreshold(config.getSimilarityThreshold());
            }
            if (config.getQueryLimit() != null) {
                qdrantVectorService.setQueryLimit(config.getQueryLimit());
            }

            ConfigInfo updatedConfig = new ConfigInfo(
                    qdrantVectorService.getSimilarityThreshold(),
                    qdrantVectorService.getQueryLimit()
            );
            return ApiResponse.ok("配置更新成功", updatedConfig);
        } catch (Exception e) {
            return ApiResponse.error(500, "更新配置时发生错误: " + e.getMessage());
        }
    }

    // 请求和响应实体类
    @Data
    public static class InsertVectorRequest {
        private String id;
        private String title;
        private List<Float> vector;
    }

    @Data
    public static class BatchInsertRequest {
        private List<VectorDataRequest> vectorDataList;
    }

    @Data
    public static class VectorDataRequest {
        private String id;
        private String title;
        private List<Float> vector;
    }

    @Data
    public static class SearchVectorRequest {
        private List<Float> queryVector;
        private Double threshold;
        private Integer limit;
    }

    @Data
    public static class ConfigInfo {
        private Double similarityThreshold;
        private Integer queryLimit;

        public ConfigInfo() {
        }

        public ConfigInfo(Double similarityThreshold, Integer queryLimit) {
            this.similarityThreshold = similarityThreshold;
            this.queryLimit = queryLimit;
        }
    }
}