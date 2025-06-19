/**
 * File Name: AiAnalysisRabbitConfig.java
 * Description: AI 解析功能的 RabbitMQ 配置类，定义了解析请求的队列、交换机、路由键以及发送逻辑。
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 自动加载配置后，可通过调用 sendMessage 方法发送 AiAnalysisMessage 类型的消息，
 * 消息将被路由到绑定的 AI 解析队列，由 AI 服务消费并执行解析任务。
 */

package org.example.serviceai.userAnswer.config;

import org.example.serviceai.userAnswer.dto.AiAnalysisMessage;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 解析功能相关的 RabbitMQ 配置
 */
@Configuration
public class AiAnalysisRabbitConfig {

    // AI 解析队列名
    public static final String AI_ANALYSIS_QUEUE = "ai.analysis.queue";
    // AI 解析交换机名
    public static final String AI_ANALYSIS_EXCHANGE = "ai.analysis.exchange";
    // AI 解析路由键
    public static final String AI_ANALYSIS_ROUTE_KEY = "ai.analysis.route";

    /**
     * 声明 AI 解析队列（持久化）
     */
    @Bean
    public Queue aiAnalysisQueue() {
        return new Queue(AI_ANALYSIS_QUEUE, true);
    }

    /**
     * 声明 AI 解析交换机
     */
    @Bean
    public DirectExchange aiAnalysisExchange() {
        return new DirectExchange(AI_ANALYSIS_EXCHANGE, true, false);
    }

    /**
     * 将 AI 解析队列绑定到交换机
     */
    @Bean
    public Binding aiAnalysisBinding(Queue aiAnalysisQueue, DirectExchange aiAnalysisExchange) {
        return BindingBuilder.bind(aiAnalysisQueue).to(aiAnalysisExchange).with(AI_ANALYSIS_ROUTE_KEY);
    }

    /**
     * 发送 AI 解析消息到指定队列
     *
     * @param rabbitTemplate  发送消息的模板
     * @param analysisMessage AI 解析消息内容（含回答ID、用户答案等）
     */
    public void sendMessage(RabbitTemplate rabbitTemplate, AiAnalysisMessage analysisMessage) {
        try {
            rabbitTemplate.convertAndSend(AI_ANALYSIS_EXCHANGE, AI_ANALYSIS_ROUTE_KEY, analysisMessage);
        } catch (Exception e) {
            throw new RuntimeException("发送 AI 解析消息失败", e);
        }
    }

}
