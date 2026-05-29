/**
 * @file QdrantClientConfig
 * @project SlothAsk
 * @module Service-Question / Qdrant
 * @description 创建题库服务使用的 Qdrant gRPC 客户端。
 * @logic 1. 从配置中心读取 Qdrant 地址与端口；2. 使用明文 gRPC 连接本地 Qdrant；3. 关闭版本兼容检查以适配当前 1.15 客户端与 1.18 服务端组合。
 * @dependencies Config: qdrant.url, Config: qdrant.port, Bean: QdrantClient
 * @index_tags Qdrant, gRPC, client, compatibility, Service-Question
 * @author holic512
 */
package org.example.servicequestion.config.Qdrant;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantClientConfig {

    @Value("${qdrant.url:localhost}")
    private String qdrantUrl;

    @Value("${qdrant.port:6334}")
    private int qdrantPort;

    @Bean
    public QdrantClient qdrantClient() {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(qdrantUrl, qdrantPort, false, false).build());
    }
}
