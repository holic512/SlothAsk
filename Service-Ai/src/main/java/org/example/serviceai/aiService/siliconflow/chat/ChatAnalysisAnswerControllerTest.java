/**
 * File Name: AnalysisAnswerControllerTest.java
 * Description: AnalysisAnswer服务的测试控制器
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * POST /ai/test/ask-question - 指定模型的AI问答测试
 * POST /ai/test/ask-question-random - 随机模型的AI问答测试
 */
package org.example.serviceai.aiService.siliconflow.chat;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AnalysisAnswer服务测试控制器
 * 提供AI问答功能的HTTP接口测试
 */
@RestController
@RequestMapping("/ai/test")
@CrossOrigin(origins = "*")
public class ChatAnalysisAnswerControllerTest {

    @Autowired
    private ChatSiliconflowAiService chatSiliconflowAiService;

    /**
     * 指定模型的AI问答测试接口
     *
     * @param request 请求参数
     * @return AI响应结果
     */
    @PostMapping("/ask-question")
    public ResponseEntity<ChatAiResponse> askQuestion(@RequestBody AskQuestionRequest request) {
        try {

            if (request.getInput() == null || request.getInput().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        ChatAiResponse.failure("input参数不能为空", request.getModel(),
                                request.getPrompt(), request.getInput(), false, 0L, request.isThink())
                );
            }

            if (request.getModel() == null) {
                return ResponseEntity.badRequest().body(
                        ChatAiResponse.failure("model参数不能为空", null,
                                request.getPrompt(), request.getInput(), false, 0L, request.isThink())
                );
            }

            // 调用AI服务
            ChatAiResponse response = chatSiliconflowAiService.askQuestion(
                    request.getModel(),
                    request.getPrompt(),
                    request.getInput(),
                    request.isThink()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ChatAiResponse.failure("服务器内部错误: " + e.getMessage(),
                            request.getModel(), request.getPrompt(),
                            request.getInput(), false, 0L, request.isThink())
            );
        }
    }

    /**
     * 随机模型的AI问答测试接口
     *
     * @param request 请求参数
     * @return AI响应结果
     */
    @PostMapping("/ask-question-random")
    public ResponseEntity<ChatAiResponse> askQuestionWithRandomModel(@RequestBody AskQuestionRandomRequest request) {
        try {

            if (request.getInput() == null || request.getInput().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        ChatAiResponse.failure("input参数不能为空", null,
                                request.getPrompt(), request.getInput(), true, 0L, request.isThink())
                );
            }

            // 调用AI服务（随机模型）
            ChatAiResponse response = chatSiliconflowAiService.askQuestionWithRandomModel(
                    request.getPrompt(),
                    request.getInput(),
                    request.isThink()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ChatAiResponse.failure("服务器内部错误: " + e.getMessage(),
                            null, request.getPrompt(),
                            request.getInput(), true, 0L, request.isThink())
            );
        }
    }

    /**
     * 获取所有可用模型列表
     *
     * @return 模型列表
     */
    @GetMapping("/models")
    public ResponseEntity<ChatSiliconflowModelEnum[]> getAvailableModels() {
        return ResponseEntity.ok(ChatSiliconflowModelEnum.values());
    }

    /**
     * 健康检查接口
     *
     * @return 服务状态
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AnalysisAnswer服务运行正常");
    }

    /**
     * 指定模型问答请求参数
     */
    @Data
    public static class AskQuestionRequest {
        private ChatSiliconflowModelEnum model;
        private String prompt;
        private String input;
        private boolean think = false;
    }

    /**
     * 随机模型问答请求参数
     */
    @Data
    public static class AskQuestionRandomRequest {
        // Getter和Setter方法
        private String prompt;
        private String input;
        private boolean think = false;
    }
}