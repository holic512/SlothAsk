/**
 * File Name: MinioConfig.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-31
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceimage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio") // 自动绑定
@Data // Lombok 注解
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String proxyUrl; // 代理地址，用于外部访问图片URL
}