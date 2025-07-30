/**
 * File Name: EmbeddingAnalysisControllerTest.java
 * Description: 嵌入向量服务的测试控制器
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * POST /ai/embedding/test/get-embedding - 指定模型的嵌入向量测试
 * POST /ai/embedding/test/get-embedding-random - 随机模型的嵌入向量测试
 * POST /ai/embedding/test/get-batch-embedding - 批量嵌入向量测试
 */
package org.example.serviceai.aiService.siliconflow.embedding;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 嵌入向量服务测试控制器
 * 提供嵌入向量功能的HTTP接口测试
 */
@RestController
@RequestMapping("/ai/embedding/test")
@CrossOrigin(origins = "*")
public class EmbeddingAnalysisControllerTest {

    @Autowired
    private EmbeddingSiliconflowAiService embeddingSiliconflowAiService;

    /**
     * 指定模型的嵌入向量测试接口
     *
     * @param request 请求参数
     * @return 嵌入向量响应结果
     */
    @PostMapping("/get-embedding")
    public ResponseEntity<EmbeddingAiResponse> getEmbedding(@RequestBody GetEmbeddingRequest request) {
        try {
            if (request.getInput() == null || request.getInput().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        EmbeddingAiResponse.failure("input参数不能为空", request.getModel(),
                                request.getInput(), false, 0L)
                );
            }

            if (request.getModel() == null) {
                return ResponseEntity.badRequest().body(
                        EmbeddingAiResponse.failure("model参数不能为空", null,
                                request.getInput(), false, 0L)
                );
            }

            // 调用嵌入服务
            EmbeddingAiResponse response = embeddingSiliconflowAiService.getEmbedding(
                    request.getModel(),
                    request.getInput()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    EmbeddingAiResponse.failure("服务器内部错误: " + e.getMessage(),
                            request.getModel(), request.getInput(), false, 0L)
            );
        }
    }

    /**
     * 随机模型的嵌入向量测试接口
     *
     * @param request 请求参数
     * @return 嵌入向量响应结果
     */
    @PostMapping("/get-embedding-random")
    public ResponseEntity<EmbeddingAiResponse> getEmbeddingWithRandomModel(@RequestBody GetEmbeddingRandomRequest request) {
        try {
            if (request.getInput() == null || request.getInput().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        EmbeddingAiResponse.failure("input参数不能为空", null,
                                request.getInput(), true, 0L)
                );
            }

            // 调用嵌入服务（随机模型）
            EmbeddingAiResponse response = embeddingSiliconflowAiService.getEmbeddingWithRandomModel(
                    request.getInput()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    EmbeddingAiResponse.failure("服务器内部错误: " + e.getMessage(),
                            null, request.getInput(), true, 0L)
            );
        }
    }

    /**
     * 批量嵌入向量测试接口
     *
     * @param request 请求参数
     * @return 嵌入向量响应结果
     */
    @PostMapping("/get-batch-embedding")
    public ResponseEntity<EmbeddingAiResponse> getBatchEmbedding(@RequestBody GetBatchEmbeddingRequest request) {
        try {
            if (request.getInputs() == null || request.getInputs().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        EmbeddingAiResponse.failure("inputs参数不能为空", request.getModel(),
                                "批量输入", false, 0L)
                );
            }

            if (request.getModel() == null) {
                return ResponseEntity.badRequest().body(
                        EmbeddingAiResponse.failure("model参数不能为空", null,
                                "批量输入", false, 0L)
                );
            }

            // 调用批量嵌入服务
            EmbeddingAiResponse response = embeddingSiliconflowAiService.getBatchEmbedding(
                    request.getModel(),
                    request.getInputs()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    EmbeddingAiResponse.failure("服务器内部错误: " + e.getMessage(),
                            request.getModel(), "批量输入", false, 0L)
            );
        }
    }

    /**
     * 获取所有可用嵌入模型列表
     *
     * @return 模型列表
     */
    @GetMapping("/models")
    public ResponseEntity<EmbeddingSiliconflowModelEnum[]> getAvailableModels() {
        return ResponseEntity.ok(EmbeddingSiliconflowModelEnum.values());
    }

    /**
     * 健康检查接口
     *
     * @return 服务状态
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("嵌入向量服务运行正常");
    }

    /**
     * 指定模型嵌入请求参数
     */
    @Data
    public static class GetEmbeddingRequest {
        private EmbeddingSiliconflowModelEnum model;
        private String input;
    }

    /**
     * 随机模型嵌入请求参数
     */
    @Data
    public static class GetEmbeddingRandomRequest {
        private String input;
    }

    /**
     * 批量嵌入请求参数
     */
    @Data
    public static class GetBatchEmbeddingRequest {
        private EmbeddingSiliconflowModelEnum model;
        private List<String> inputs;
    }
}