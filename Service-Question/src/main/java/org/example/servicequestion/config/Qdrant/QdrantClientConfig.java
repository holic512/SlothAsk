/**
 * File Name: QdrantClientConfig.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-07-29
 * Version: 1.0
 * Usage:
 * Todo
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
                QdrantGrpcClient.newBuilder(qdrantUrl, qdrantPort, false).build());
    }
}
