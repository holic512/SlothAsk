/**
 * File Name: AiAnalysisConsumer.java
 * Description: AI 解析功能的 RabbitMQ 消费者，用于接收和处理 AI 解析消息。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 自动监听 AI 解析队列，接收 AiAnalysisMessage 类型的消息并进行处理。
 */

package org.example.serviceai.userAnswer.consumer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.serviceai.aiService.siliconflow.AiResponse;
import org.example.serviceai.aiService.siliconflow.SiliconflowAiService;
import org.example.serviceai.userAnswer.config.AiAnalysisRabbitConfig;
import org.example.serviceai.userAnswer.dto.AiAnalysisMessage;
import org.example.serviceai.userAnswer.dto.AiAnalysisRequest;
import org.example.serviceai.userAnswer.dto.AiAnalysisResponse;
import org.example.serviceai.userAnswer.mapper.QuestionMapper;
import org.example.serviceai.userAnswer.mapper.UAAiAnalysisMapper;
import org.example.serviceai.userAnswer.mapper.UQRecordMapper;
import org.example.serviceai.util.HtmlTextExtractor;
import org.example.servicecommon.entity.Question;
import org.example.servicecommon.entity.UserAnswerAiAnalysis;
import org.example.servicecommon.entity.UserQuestionRecord;
import org.example.servicecommon.redisKey.AnswerAiKey;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AI 解析消息消费者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiAnalysisConsumer {

    private final UAAiAnalysisMapper uaAiAnalysisMapper;
    private final UQRecordMapper uqRecordMapper;
    private final QuestionMapper questionMapper;
    private final SiliconflowAiService siliconflowAiService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 监听 AI 解析队列，处理接收到的消息（手动确认模式，3个线程并发）
     *
     * @param analysisMessage AI 解析消息
     * @param deliveryTag     消息投递标签
     * @param channel         RabbitMQ 通道
     */
    @RabbitListener(
            queues = AiAnalysisRabbitConfig.AI_ANALYSIS_QUEUE,
            ackMode = "MANUAL",
            concurrency = "3",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleAiAnalysisMessage(@Payload AiAnalysisMessage analysisMessage,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                        Channel channel) {
        String answerId = analysisMessage.getAnswerId();
        String threadName = Thread.currentThread().getName();

        try {
            log.info("接收到 AI 解析消息，回答ID: {}, 线程: {}", answerId, threadName);

            // 处理 AI 解析业务逻辑
            processAiAnalysis(answerId);

            // 解除 ai解析中 的 redis锁
            releaseAiAnalysisLock(answerId);

            log.info("AI 解析消息处理完成，回答ID: {}, 线程: {}", answerId, threadName);
            // 手动确认消息
            acknowledgeMessage(channel, deliveryTag);

        } catch (Exception e) {
            log.error("处理 AI 解析消息失败，回答ID: {}, 线程: {}", answerId, threadName, e);

            // 即使处理失败也要解除 Redis 状态锁，避免死锁
            releaseAiAnalysisLock(answerId);

            // 拒绝消息并重新入队
            rejectMessage(channel, deliveryTag);
            // TODO: 可以考虑添加重试机制或死信队列处理
        }
    }

    /**
     * 处理 AI 解析业务逻辑
     *
     * @param answerId 回答ID
     * @throws Exception 处理异常
     */
    private void processAiAnalysis(String answerId) throws Exception {
        // 1. 获取用户回答记录和问题信息
        UserQuestionRecord record = getUserQuestionRecord(answerId);
        Question question = getQuestion(record.getQuestionId());

        // 2. 构建 AI 分析请求
        AiAnalysisRequest analysisRequest = buildAnalysisRequest(question, record.getUserAnswer());

        // 3. 调用 AI 服务进行分析
        AiResponse aiResponse = callAiService(analysisRequest);

        // 4. 解析 AI 响应并保存结果
        AiAnalysisResponse analysisResponse = parseAiResponse(aiResponse.getAnswer());
        saveAnalysisResult(answerId, analysisResponse, aiResponse.getModelFull(), analysisRequest);
    }

    /**
     * 根据回答ID获取用户问题记录
     *
     * @param answerId 回答ID
     * @return 用户问题记录
     */
    private UserQuestionRecord getUserQuestionRecord(String answerId) {
        LambdaQueryWrapper<UserQuestionRecord> queryWrapper = new LambdaQueryWrapper<UserQuestionRecord>()
                .eq(UserQuestionRecord::getId, answerId);
        return uqRecordMapper.selectOne(queryWrapper);
    }

    /**
     * 根据问题ID获取问题信息
     *
     * @param questionId 问题ID
     * @return 问题信息
     * @throws IllegalArgumentException 当问题ID为空时抛出异常
     */
    private Question getQuestion(Long questionId) {
        if (questionId == null) {
            throw new IllegalArgumentException("问题ID不能为空");
        }

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<Question>()
                .eq(Question::getId, questionId)
                .select(Question::getContent, Question::getAnswer);
        return questionMapper.selectOne(queryWrapper);
    }

    /**
     * 构建 AI 分析请求
     *
     * @param question   问题信息
     * @param userAnswer 用户答案
     * @return AI 分析请求对象
     */
    private AiAnalysisRequest buildAnalysisRequest(Question question, String userAnswer) {
        AiAnalysisRequest analysisRequest = new AiAnalysisRequest();
        analysisRequest.setQuestion(HtmlTextExtractor.extractPlainTextWithNewlines(question.getContent()));
        analysisRequest.setCorrectAnswer(HtmlTextExtractor.extractPlainTextWithNewlines(question.getAnswer()));
        analysisRequest.setStudentAnswer(userAnswer);
        return analysisRequest;
    }

    /**
     * 调用 AI 服务进行分析
     *
     * @param analysisRequest AI 分析请求
     * @return AI 响应
     */
    private AiResponse callAiService(AiAnalysisRequest analysisRequest) {
        String systemPrompt = buildSystemPrompt();
        return siliconflowAiService.askQuestionWithRandomModel(
                systemPrompt,
                analysisRequest.toString(),
                true
        );
    }

    /**
     * 构建系统提示词
     *
     * @return 系统提示词
     */
    private String buildSystemPrompt() {
        return "你是一个严谨且专业的教师 AI，擅长评估学生对知识点的掌握程度。\n" +
                "你的任务是：根据学生提交的回答，分析其理解是否正确，并返回以下两项内容：\n" +
                "1. 准确率（accuracy）：范围为 0～100，表示学生回答的正确程度。请从内容完整性、表达准确性、逻辑合理性等维度综合评估；\n" +
                "2. 具体分析（analysis）：指出学生回答中的错误、遗漏或理解偏差，说明正确知识点，必要时可分点列出（每点用 <br/> 分隔）。\n" +
                "你需要在评分前先认真比对正确答案与学生答案的异同，充分理解语义，再进行分析与评分。\n" +
                "输入格式（标准 JSON）：\n" +
                "{\n" +
                "  \"question\": \"Java 是解释型语言还是编译型语言？请说明原因。\",\n" +
                "  \"correctAnswer\": \"Java 同时具有编译型和解释型的特性。Java 源代码首先会被编译为字节码（.class 文件），然后由 Java 虚拟机（JVM）解释执行。\",\n" +
                "  \"studentAnswer\": \"Java 是一种编译型语言，它会被编译成机器码后执行。\"\n" +
                "}\n" +
                "输出格式（标准 JSON）：\n" +
                "{\n" +
                "  \"accuracy\": 60,\n" +
                "  \"analysis\": \"你提到 Java 是编译型语言，但这种说法并不准确。<br/><b>Java 的源代码会被编译为字节码，而不是机器码</b>，然后由 Java 虚拟机解释执行，因此它既具有编译型特性，也具有解释型特性。\"\n" +
                "}\n" +
                "输出要求(重点,一定要遵顼)：\n" +
                "- 只返回标准 JSON 格式；\n" +
                "- 不包含额外描述、解释或非 JSON 内容；\n" +
                "- 必须使用双引号包裹字段名和字符串；\n" +
                "- 分析部分可使用 <br/> 换行，<b> 标签加粗重点；\n" +
                "- 严格保证 JSON 格式合法，避免语法错误。\n" +
                "- 请只返回标准 JSON 格式，不要包含代码块、Markdown 标记（如 ```json）或任何额外说明文字。";
    }


    /**
     * 解析 AI 响应
     *
     * @param aiResponseJson AI 响应的 JSON 字符串
     * @return AI 分析响应对象
     * @throws Exception JSON 解析异常
     */
    private AiAnalysisResponse parseAiResponse(String aiResponseJson) throws Exception {
        try {
            // DEBUG：打印原始返回值（限制长度避免日志过大）
            String preview = aiResponseJson == null ? "null" :
                    aiResponseJson.length() > 500 ? aiResponseJson.substring(0, 500) + "..." : aiResponseJson;
            log.debug("AI 原始响应内容预览（前500字符）：{}", preview);

            // DEBUG：判断是否包含非法 Markdown、反引号
            if (aiResponseJson.contains("```") || aiResponseJson.contains("`")) {
                log.warn("⚠️ AI 响应中包含 Markdown 代码块标记或反引号字符");
            }

            // DEBUG：尝试格式修复 - 去除 Markdown 包裹内容（可选）
            if (aiResponseJson.trim().startsWith("```")) {
                int start = aiResponseJson.indexOf("{");
                int end = aiResponseJson.lastIndexOf("}");
                if (start >= 0 && end > start) {
                    String trimmed = aiResponseJson.substring(start, end + 1);
                    log.warn("⚠️ 检测到 Markdown 包裹，尝试修复并解析 JSON");
                    aiResponseJson = trimmed;
                }
            }

            // 正常解析
            return objectMapper.readValue(aiResponseJson, AiAnalysisResponse.class);

        } catch (Exception e) {
            log.error("❌ JSON 解析失败，原始返回内容：{}", aiResponseJson, e);
            throw e;
        }
    }

    /**
     * 保存分析结果到数据库
     *
     * @param answerId         回答ID
     * @param analysisResponse AI 分析响应
     * @param aiModel          AI 模型信息
     */
    private void saveAnalysisResult(String answerId, AiAnalysisResponse analysisResponse, String aiModel, AiAnalysisRequest analysisRequest) {
        UserAnswerAiAnalysis userAnswerAiAnalysis = new UserAnswerAiAnalysis();
        userAnswerAiAnalysis.setRecordId(Long.parseLong(answerId));
        userAnswerAiAnalysis.setAccuracyRate(analysisResponse.getAccuracy());
        userAnswerAiAnalysis.setAiExplanation(analysisResponse.getAnalysis());
        userAnswerAiAnalysis.setAiSource(aiModel);
        userAnswerAiAnalysis.setQuestionContent(analysisRequest.getQuestion());
        userAnswerAiAnalysis.setQuestionAnswer(analysisRequest.getCorrectAnswer());
        userAnswerAiAnalysis.setUserAnswer(analysisRequest.getStudentAnswer());
        uaAiAnalysisMapper.insert(userAnswerAiAnalysis);
    }

    /**
     * 确认消息处理成功
     *
     * @param channel     RabbitMQ 通道
     * @param deliveryTag 消息投递标签
     */
    private void acknowledgeMessage(Channel channel, long deliveryTag) {
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error("确认消息失败", e);
        }
    }

    /**
     * 拒绝消息并重新入队
     *
     * @param channel     RabbitMQ 通道
     * @param deliveryTag 消息投递标签
     */
    private void rejectMessage(Channel channel, long deliveryTag) {
        try {
            channel.basicNack(deliveryTag, false, true);
        } catch (IOException e) {
            log.error("拒绝消息失败", e);
        }
    }

    /**
     * 解除 AI 解析状态锁
     *
     * @param answerId 回答ID
     */
    private void releaseAiAnalysisLock(String answerId) {
        try {
            String lockKey = AnswerAiKey.ANSWER_AI_PARSING_KEY + answerId;
            Boolean deleted = redisTemplate.delete(lockKey);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("成功解除 AI 解析状态锁，回答ID: {}", answerId);
            } else {
                log.warn("AI 解析状态锁不存在或已过期，回答ID: {}", answerId);
            }
        } catch (Exception e) {
            log.error("解除 AI 解析状态锁失败，回答ID: {}", answerId, e);
        }
    }
}