/**
 * File Name: AiResponse.java
 * Description: AI服务响应DTO类，封装AI问答的完整响应信息
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.1  <!-- 版本号升级 -->
 * Usage:
 * AiResponse response = new AiResponse(true, "答案内容", SiliconflowModelEnum.QWEN3_8B, "prompt内容", "input内容", true, 1500L, true);
 */
package org.example.serviceai.aiService.siliconflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI服务响应数据传输对象
 * 包含操作状态、AI回答、模型信息、请求参数和执行时间等完整信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiResponse {

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 从AI服务获取的答案内容,ai的回答
     */
    private String answer;

    /**
     * 随机选择的模型枚举名称（如QWEN3_8B）
     */
    private SiliconflowModelEnum model;

    /**
     * 完整模型路径（如Qwen/Qwen3-8B）
     */
    private String modelFull;

    /**
     * 从请求获取的系统提示词
     */
    private String prompt;

    /**
     * 从请求获取的用户输入内容
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
     * 是否开启AI思考过程（如逐步推理、思维链等）
     */
    private boolean enableThinking;

    /**
     * 构造方法：基于模型枚举自动设置完整模型路径
     *
     * @param success 操作是否成功
     * @param answer AI回答内容
     * @param model 模型枚举
     * @param prompt 系统提示词
     * @param input 用户输入
     * @param isRandom 是否随机选择
     * @param durationMs 执行时间（毫秒）
     * @param enableThinking 是否开启AI思考过程
     */
    public AiResponse(boolean success, String answer, SiliconflowModelEnum model,
                      String prompt, String input, boolean isRandom, long durationMs, boolean enableThinking) {
        this.success = success;
        this.answer = answer;
        this.model = model;
        this.modelFull = model != null ? model.getModelName() : null;
        this.prompt = prompt;
        this.input = input;
        this.isRandom = isRandom;
        this.durationMs = durationMs;
        this.enableThinking = enableThinking;
    }

    /**
     * 创建成功响应的静态工厂方法
     *
     * @param answer AI回答内容
     * @param model 使用的模型
     * @param prompt 系统提示词
     * @param input 用户输入
     * @param isRandom 是否随机选择
     * @param durationMs 执行时间
     * @param enableThinking 是否开启AI思考过程
     * @return 成功的AiResponse实例
     */
    public static AiResponse success(String answer, SiliconflowModelEnum model,
                                     String prompt, String input, boolean isRandom, long durationMs, boolean enableThinking) {
        return new AiResponse(true, answer, model, prompt, input, isRandom, durationMs, enableThinking);
    }

    /**
     * 创建失败响应的静态工厂方法
     *
     * @param errorMessage 错误信息
     * @param model 尝试使用的模型
     * @param prompt 系统提示词
     * @param input 用户输入
     * @param isRandom 是否随机选择
     * @param durationMs 执行时间
     * @param enableThinking 是否开启AI思考过程
     * @return 失败的AiResponse实例
     */
    public static AiResponse failure(String errorMessage, SiliconflowModelEnum model,
                                     String prompt, String input, boolean isRandom, long durationMs, boolean enableThinking) {
        return new AiResponse(false, errorMessage, model, prompt, input, isRandom, durationMs, enableThinking);
    }
}