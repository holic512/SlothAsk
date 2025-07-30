/**
 * File Name: UserSearchEmbeddingController.java
 * Description: 用户搜索嵌入向量服务正式接口，供其他服务调用
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * POST /ai/user-search/embedding/single - 单体文本嵌入转换
 * POST /ai/user-search/embedding/batch - 批量文本嵌入转换
 */
package org.example.serviceai.userSearch;

import lombok.Data;
import org.example.serviceai.aiService.siliconflow.embedding.EmbeddingAiResponse;
import org.example.serviceai.aiService.siliconflow.embedding.EmbeddingSiliconflowAiService;
import org.example.serviceai.aiService.siliconflow.embedding.EmbeddingSiliconflowModelEnum;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户搜索嵌入向量服务控制器
 * 提供单体和批量文本嵌入转换的正式API接口
 */
@RestController
@RequestMapping("/ai/user-search/embedding")
@CrossOrigin(origins = "*")
public class UserSearchEmbeddingController {

    /**
     * 固定使用的嵌入模型
     */
    private static final EmbeddingSiliconflowModelEnum FIXED_MODEL = EmbeddingSiliconflowModelEnum.QWEN3_EMBEDDING_8B;
    @Autowired
    private EmbeddingSiliconflowAiService embeddingSiliconflowAiService;

    /**
     * 单体文本嵌入转换接口
     * 使用固定的Qwen3-Embedding-8B模型进行文本嵌入
     *
     * @param request 单体嵌入请求参数
     * @return 嵌入向量响应结果 为 List<List<Float>> embeddings
     */
    @PostMapping("/single")
    public ApiResponse getSingleEmbedding(@RequestBody SingleEmbeddingRequest request) {
        try {
            // 参数验证
            if (request.getText() == null || request.getText().trim().isEmpty()) {
                return ApiResponse.error(400, "文本内容不能为空");
            }

            // 调用嵌入服务
            EmbeddingAiResponse response = embeddingSiliconflowAiService.getEmbedding(FIXED_MODEL, request.getText());

            if (response.isSuccess()) {
                return ApiResponse.ok("嵌入向量生成成功", response.getEmbeddings());
            } else {
                return ApiResponse.error(500, response.getErrorMessage());
            }

        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 批量文本嵌入转换接口
     * 使用固定的Qwen3-Embedding-8B模型进行批量文本嵌入
     *
     * @param request 批量嵌入请求参数
     * @return 嵌入向量响应结果 List<List<Float>> embeddings
     */
    @PostMapping("/batch")
    public ApiResponse getBatchEmbedding(@RequestBody BatchEmbeddingRequest request) {
        try {
            // 参数验证
            if (request.getTexts() == null || request.getTexts().isEmpty()) {
                return ApiResponse.error(400, "文本列表不能为空");
            }

            // 检查是否有空文本
            for (String text : request.getTexts()) {
                if (text == null || text.trim().isEmpty()) {
                    return ApiResponse.error(400, "文本列表中不能包含空文本");
                }
            }

            // 调用批量嵌入服务
            EmbeddingAiResponse response = embeddingSiliconflowAiService.getBatchEmbedding(FIXED_MODEL, request.getTexts());

            if (response.isSuccess()) {
                return ApiResponse.ok("批量嵌入向量生成成功", response.getEmbeddings());
            } else {
                return ApiResponse.error(500, response.getErrorMessage());
            }

        } catch (Exception e) {
            return ApiResponse.error(500, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取服务状态
     *
     * @return 服务状态信息
     */
    @GetMapping("/status")
    public ApiResponse getServiceStatus() {
        ServiceStatus status = new ServiceStatus();
        status.setServiceName("用户搜索嵌入向量服务");
        status.setStatus("运行正常");
        status.setFixedModel(FIXED_MODEL.getModelName());
        return ApiResponse.ok("服务状态获取成功", status);
    }

    /**
     * 单体嵌入请求参数
     */
    @Data
    public static class SingleEmbeddingRequest {
        /**
         * 要转换为嵌入向量的文本内容
         */
        private String text;
    }

    /**
     * 批量嵌入请求参数
     */
    @Data
    public static class BatchEmbeddingRequest {
        /**
         * 要转换为嵌入向量的文本列表
         */
        private List<String> texts;
    }


    /**
     * 服务状态响应
     */
    @Data
    public static class ServiceStatus {
        private String serviceName;
        private String status;
        private String fixedModel;
    }
}