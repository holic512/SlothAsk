/**
 * File Name: ServiceAiFeign.java
 * Description: AI服务Feign客户端接口，用于与AI微服务进行远程调用
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * 提供文本嵌入向量转换、批量嵌入转换和服务状态查询等功能，通过OpenFeign实现微服务间的通信
 */
package org.example.servicequestion.feign;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-ai")
public interface ServiceAiFeign {

    /**
     * 单体文本嵌入转换接口
     *
     * @param request 单体嵌入请求参数
     * @return 嵌入向量响应结果
     */
    @PostMapping("/ai/user-search/embedding/single")
    ApiResponse getSingleEmbedding(@RequestBody SingleEmbeddingRequest request);

    /**
     * 批量文本嵌入转换接口
     *
     * @param request 批量嵌入请求参数
     * @return 嵌入向量响应结果
     */
    @PostMapping("/ai/user-search/embedding/batch")
    ApiResponse getBatchEmbedding(@RequestBody BatchEmbeddingRequest request);

    /**
     * 获取AI服务状态
     *
     * @return 服务状态信息
     */
    @GetMapping("/ai/user-search/embedding/status")
    ApiResponse getServiceStatus();

    /**
     * 单体嵌入请求参数
     */
    class SingleEmbeddingRequest {
        /**
         * 要转换为嵌入向量的文本内容
         */
        private String text;

        public SingleEmbeddingRequest() {}

        public SingleEmbeddingRequest(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * 批量嵌入请求参数
     */
    class BatchEmbeddingRequest {
        /**
         * 要转换为嵌入向量的文本列表
         */
        private List<String> texts;

        public BatchEmbeddingRequest() {}

        public BatchEmbeddingRequest(List<String> texts) {
            this.texts = texts;
        }

        public List<String> getTexts() {
            return texts;
        }

        public void setTexts(List<String> texts) {
            this.texts = texts;
        }
    }
}