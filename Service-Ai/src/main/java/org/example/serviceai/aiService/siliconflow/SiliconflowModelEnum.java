/**
 * File Name: SiliconflowModelEnum.java
 * Description: 硅基流动的AI模型枚举，定义可供调用的模型名称
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * SiliconflowModelEnum.QWEN_32B.getModelName()
 */
package org.example.serviceai.aiService.siliconflow;

import lombok.Getter;

@Getter
public enum SiliconflowModelEnum {

    /**
     * 通义千问 Qwen3-32B 模型
     */
    QWEN3_32B("Qwen/Qwen3-32B"),

    /**
     * 通义千问 Qwen3-8B 模型
     */
    QWEN3_8B("Qwen/Qwen3-8B"),

    /**
     * DeepSeek 基于 Qwen3-8B 的 R1-0528 版本模型
     */
    DEEPSEEK_R1_0528_QWEN3_8B("deepseek-ai/DeepSeek-R1-0528-Qwen3-8B"),

    /**
     * 通义千问 Qwen3-14B 模型
     */
    QWEN3_14B("Qwen/Qwen3-14B");

    private final String modelName;

    SiliconflowModelEnum(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return modelName;
    }
}