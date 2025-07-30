/**
 * File Name: EmbeddingSiliconflowAiService.java
 * Description: SiliconFlow AI嵌入服务封装类
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * EmbeddingSiliconflowAiService.getEmbedding(EmbeddingSiliconflowModelEnum.QWEN3_EMBEDDING_8B, "文本内容")
 * EmbeddingSiliconflowAiService.getEmbeddingWithRandomModel("文本内容")
 */
package org.example.serviceai.aiService.siliconflow.embedding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class EmbeddingSiliconflowAiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    
    @Autowired
    private OkHttpClient okHttpClient;
    
    @Value("${siliconflow.api.key:}")
    private String apiKey;
    
    @Value("${siliconflow.api.embeddings.url:https://api.siliconflow.cn/v1/embeddings}")
    private String apiUrl;

    /**
     * 指定模型的嵌入向量请求
     *
     * @param model 指定的嵌入模型枚举
     * @param input 输入文本内容
     * @return 完整的嵌入响应对象
     */
    public EmbeddingAiResponse getEmbedding(EmbeddingSiliconflowModelEnum model, String input) {
        return performEmbeddingRequestWithResponse(model, input, false);
    }

    /**
     * 指定模型的批量嵌入向量请求
     *
     * @param model 指定的嵌入模型枚举
     * @param inputs 输入文本列表
     * @return 完整的嵌入响应对象
     */
    public EmbeddingAiResponse getBatchEmbedding(EmbeddingSiliconflowModelEnum model, List<String> inputs) {
        return performBatchEmbeddingRequestWithResponse(model, inputs, false);
    }

    /**
     * 随机选择模型的嵌入向量请求
     *
     * @param input 输入文本内容
     * @return 完整的嵌入响应对象
     */
    public EmbeddingAiResponse getEmbeddingWithRandomModel(String input) {
        EmbeddingSiliconflowModelEnum randomModel = getRandomModel();
        return performEmbeddingRequestWithResponse(randomModel, input, true);
    }

    /**
     * 统一的嵌入请求处理方法（单个文本）
     *
     * @param model    嵌入模型枚举
     * @param input    输入文本内容
     * @param isRandom 是否为随机选择的模型
     * @return 完整的嵌入响应对象
     */
    private EmbeddingAiResponse performEmbeddingRequestWithResponse(EmbeddingSiliconflowModelEnum model, String input, boolean isRandom) {
        return performBatchEmbeddingRequestWithResponse(model, List.of(input), isRandom);
    }

    /**
     * 统一的嵌入请求处理方法（批量文本）
     *
     * @param model    嵌入模型枚举
     * @param inputs   输入文本列表
     * @param isRandom 是否为随机选择的模型
     * @return 完整的嵌入响应对象
     */
    private EmbeddingAiResponse performBatchEmbeddingRequestWithResponse(EmbeddingSiliconflowModelEnum model, List<String> inputs, boolean isRandom) {
        long startTime = System.currentTimeMillis();
        String inputText = String.join(", ", inputs);

        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model.getModelName());
            requestBody.put("input", inputs.size() == 1 ? inputs.get(0) : inputs);

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
                    return EmbeddingAiResponse.failure(errorMsg, model, inputText, isRandom, duration);
                }

                String responseBody = response.body().string();
                List<List<Float>> embeddings = parseEmbeddingResponse(responseBody);

                return EmbeddingAiResponse.success(embeddings, model, inputText, isRandom, duration);
            }

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            String errorMsg = "请求处理异常: " + e.getMessage();
            return EmbeddingAiResponse.failure(errorMsg, model, inputText, isRandom, duration);
        }
    }

    /**
     * 随机选择一个可用的嵌入模型
     *
     * @return 随机选择的模型枚举
     */
    private EmbeddingSiliconflowModelEnum getRandomModel() {
        EmbeddingSiliconflowModelEnum[] models = EmbeddingSiliconflowModelEnum.values();
        int randomIndex = random.nextInt(models.length);
        return models[randomIndex];
    }

    /**
     * 解析嵌入API响应，提取嵌入向量
     *
     * @param responseBody API响应的JSON字符串
     * @return 嵌入向量列表
     * @throws IOException      JSON解析异常
     * @throws RuntimeException API响应解析异常
     */
    private List<List<Float>> parseEmbeddingResponse(String responseBody) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 检查是否有错误
        if (jsonNode.has("error")) {
            String errorMessage = jsonNode.get("error").get("message").asText();
            throw new RuntimeException("API返回错误: " + errorMessage);
        }

        // 提取嵌入向量
        JsonNode data = jsonNode.get("data");
        if (data != null && data.isArray()) {
            List<List<Float>> embeddings = new ArrayList<>();
            
            for (JsonNode item : data) {
                JsonNode embedding = item.get("embedding");
                if (embedding != null && embedding.isArray()) {
                    List<Float> vector = new ArrayList<>();
                    for (JsonNode value : embedding) {
                        vector.add((float) value.asDouble());
                    }
                    embeddings.add(vector);
                }
            }
            
            if (!embeddings.isEmpty()) {
                return embeddings;
            }
        }

        throw new RuntimeException("无法从API响应中提取嵌入向量");
    }
}
