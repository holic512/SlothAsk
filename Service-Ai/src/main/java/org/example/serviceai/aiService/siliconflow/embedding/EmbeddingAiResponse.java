/**
 * File Name: EmbeddingAiResponse.java
 * Description: AI嵌入服务响应DTO类，封装嵌入向量的完整响应信息
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * EmbeddingAiResponse response = new EmbeddingAiResponse(true, embeddings, EmbeddingSiliconflowModelEnum.QWEN3_EMBEDDING_8B, "input text", false, 1500L);
 */
package org.example.serviceai.aiService.siliconflow.embedding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI嵌入服务响应数据传输对象
 * 包含操作状态、嵌入向量、模型信息、请求参数和执行时间等完整信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingAiResponse {

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 从AI服务获取的嵌入向量列表
     */
    private List<List<Float>> embeddings;

    /**
     * 使用的嵌入模型枚举
     */
    private EmbeddingSiliconflowModelEnum model;

    /**
     * 完整模型路径（如Qwen/Qwen3-Embedding-8B）
     */
    private String modelFull;

    /**
     * 输入的文本内容
     */
    private String input;

    /**
     * 标记是否为随机选择的模型
     */
    private boolean isRandom;

    /**
     * 从AI响应获取的持续时间（毫秒）
     */
    private long durationMs;

    /**
     * 错误信息（当success为false时）
     */
    private String errorMessage;

    /**
     * 构造方法：基于模型枚举自动设置完整模型路径
     *
     * @param success 操作是否成功
     * @param embeddings 嵌入向量列表
     * @param model 模型枚举
     * @param input 输入文本
     * @param isRandom 是否随机选择
     * @param durationMs 执行时间（毫秒）
     */
    public EmbeddingAiResponse(boolean success, List<List<Float>> embeddings, EmbeddingSiliconflowModelEnum model,
                              String input, boolean isRandom, long durationMs) {
        this.success = success;
        this.embeddings = embeddings;
        this.model = model;
        this.modelFull = model != null ? model.getModelName() : null;
        this.input = input;
        this.isRandom = isRandom;
        this.durationMs = durationMs;
    }

    /**
     * 创建成功响应的静态工厂方法
     *
     * @param embeddings 嵌入向量列表
     * @param model 使用的模型
     * @param input 输入文本
     * @param isRandom 是否随机选择
     * @param durationMs 执行时间
     * @return 成功的EmbeddingAiResponse实例
     */
    public static EmbeddingAiResponse success(List<List<Float>> embeddings, EmbeddingSiliconflowModelEnum model,
                                             String input, boolean isRandom, long durationMs) {
        return new EmbeddingAiResponse(true, embeddings, model, input, isRandom, durationMs);
    }

    /**
     * 创建失败响应的静态工厂方法
     *
     * @param errorMessage 错误信息
     * @param model 尝试使用的模型
     * @param input 输入文本
     * @param isRandom 是否随机选择
     * @param durationMs 执行时间
     * @return 失败的EmbeddingAiResponse实例
     */
    public static EmbeddingAiResponse failure(String errorMessage, EmbeddingSiliconflowModelEnum model,
                                             String input, boolean isRandom, long durationMs) {
        EmbeddingAiResponse response = new EmbeddingAiResponse(false, null, model, input, isRandom, durationMs);
        response.setErrorMessage(errorMessage);
        return response;
    }
}