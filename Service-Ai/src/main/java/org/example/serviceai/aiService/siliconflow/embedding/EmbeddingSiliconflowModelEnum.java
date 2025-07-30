/**
 * File Name: EmbeddingSiliconflowModelEnum.java
 * Description: 硅基流动的AI嵌入模型枚举，定义可供调用的嵌入模型名称
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * EmbeddingSiliconflowModelEnum.QWEN3_EMBEDDING_8B.getModelName()
 */
package org.example.serviceai.aiService.siliconflow.embedding;

import lombok.Getter;

@Getter
public enum EmbeddingSiliconflowModelEnum {

    /**
     * 通义千问 Qwen3-Embedding-8B 嵌入模型
     */
    QWEN3_EMBEDDING_8B("Qwen/Qwen3-Embedding-8B"),

    /**
     * 通义千问 Qwen3-Embedding-4B 嵌入模型
     */
    QWEN3_EMBEDDING_4B("Qwen/Qwen3-Embedding-4B"),

    /**
     * 通义千问 Qwen3-Embedding-0.6B 嵌入模型
     */
    QWEN3_EMBEDDING_0_6B("Qwen/Qwen3-Embedding-0.6B");


    private final String modelName;

    EmbeddingSiliconflowModelEnum(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return modelName;
    }
}