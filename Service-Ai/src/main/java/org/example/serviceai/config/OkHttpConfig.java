/**
 * File Name: OkHttpConfig.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceai.config;


import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)  // 连接超时
                .readTimeout(5, TimeUnit.MINUTES)     // 读取超时
                .writeTimeout(5, TimeUnit.MINUTES)    // 写入超时
                // 这里可以添加拦截器等配置
                .build();
    }
}
