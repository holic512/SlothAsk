/**
 * File Name: AnalysisAnswer.java
 * Description: SiliconFlow AI问答服务封装类 - 重构版本
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 2.0
 * Usage:
 * AnalysisAnswer.askQuestion(SiliconflowModelEnum.QWEN3_8B, "你是一个AI助手", "你好", false)
 * AnalysisAnswer.askQuestionWithRandomModel("你是一个AI助手", "你好", true)
 */
package org.example.serviceai.aiService.siliconflow.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ChatSiliconflowAiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    @Autowired
    private OkHttpClient okHttpClient;
    @Value("${siliconflow.api.key:}")
    private String apiKey;
    @Value("${siliconflow.api.url:https://api.siliconflow.cn/v1/chat/completions}")
    private String apiUrl;

    /**
     * 指定模型的AI问答请求
     *
     * @param model  指定的AI模型枚举
     * @param prompt 系统提示词
     * @param input  用户输入内容
     * @param think  是否开启思考模式
     * @return 完整的AI响应对象
     */
    public ChatAiResponse askQuestion(ChatSiliconflowModelEnum model, String prompt, String input, boolean think) {
        return performAiRequestWithResponse(model, prompt, input, think, false);
    }

    /**
     * 随机选择模型的AI问答请求
     *
     * @param prompt 系统提示词
     * @param input  用户输入内容
     * @param think  是否开启思考模式
     * @return 完整的AI响应对象
     */
    public ChatAiResponse askQuestionWithRandomModel(String prompt, String input, boolean think) {
        ChatSiliconflowModelEnum randomModel = getRandomModel();
        return performAiRequestWithResponse(randomModel, prompt, input, think, true);
    }

    /**
     * 统一的AI请求处理方法（公用代理方法）
     *
     * @param model    AI模型枚举
     * @param prompt   系统提示词
     * @param input    用户输入内容
     * @param think    是否开启思考模式
     * @param isRandom 是否为随机选择的模型
     * @return 完整的AI响应对象
     */
    private ChatAiResponse performAiRequestWithResponse(ChatSiliconflowModelEnum model, String prompt, String input, boolean think, boolean isRandom) {
        long startTime = System.currentTimeMillis();

        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model.getModelName());

            // 如果开启思考模式，添加相应参数
            if (think) {
                requestBody.put("reasoning_effort", "medium"); // 思考强度
                requestBody.put("enable_reasoning", true);     // 启用推理
            }

            // 构建消息列表
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", prompt);

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", input);

            requestBody.put("messages", List.of(systemMessage, userMessage));

            // 转换为JSON字符串
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            // 构建HTTP请求
            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // 发送请求并处理响应
            try (Response response = okHttpClient.newCall(request).execute()) {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                if (!response.isSuccessful()) {
                    String errorMsg = "API请求失败: " + response.code() + " - " + response.message();
                    return ChatAiResponse.failure(errorMsg, model, prompt, input, isRandom, duration, think);
                }

                String responseBody = response.body().string();
                String answer = parseResponse(responseBody);

                return ChatAiResponse.success(answer, model, prompt, input, isRandom, duration, think);
            }

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            String errorMsg = "请求处理异常: " + e.getMessage();
            return ChatAiResponse.failure(errorMsg, model, prompt, input, isRandom, duration, think);
        }
    }

    /**
     * 随机选择一个可用的模型
     *
     * @return 随机选择的模型枚举
     */
    private ChatSiliconflowModelEnum getRandomModel() {
        ChatSiliconflowModelEnum[] models = ChatSiliconflowModelEnum.values();
        int randomIndex = random.nextInt(models.length);
        return models[randomIndex];
    }

    /**
     * 解析API响应，提取AI回答内容
     *
     * @param responseBody API响应的JSON字符串
     * @return AI回答的文本内容
     * @throws IOException      JSON解析异常
     * @throws RuntimeException API响应解析异常
     */
    private String parseResponse(String responseBody) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 检查是否有错误
        if (jsonNode.has("error")) {
            String errorMessage = jsonNode.get("error").get("message").asText();
            throw new RuntimeException("API返回错误: " + errorMessage);
        }

        // 提取回答内容
        JsonNode choices = jsonNode.get("choices");
        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode firstChoice = choices.get(0);
            JsonNode message = firstChoice.get("message");
            if (message != null && message.has("content")) {
                return message.get("content").asText();
            }
        }

        throw new RuntimeException("无法从API响应中提取回答内容");
    }
}
