package org.example.serviceuser.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class OkHttpConfig {

    /**
     * 创建并注入 OkHttpClient Bean
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)

                .retryOnConnectionFailure(true)

                .connectionPool(new ConnectionPool(50, 5, TimeUnit.MINUTES))

                .dns(customDns())

                .addInterceptor(loggingInterceptor())
                .addInterceptor(customErrorInterceptor())

                // 如有需要可开启代理或信任所有证书（用于开发环境）
                .sslSocketFactory(trustAllSslSocketFactory(), trustAllManager())
                .hostnameVerifier((hostname, session) -> true)

                .build();
    }

    /**
     * DNS解析器，优先使用自定义的 IP
     */
    private Dns customDns() {
        return hostname -> {
            // if ("github.com".equalsIgnoreCase(hostname)) {
            //     log.info("使用自定义DNS解析GitHub域名: {}", hostname);
            //     return List.of(InetAddress.getByName("140.82.121.4"));
            // }
            return Dns.SYSTEM.lookup(hostname);
        };
    }

    /**
     * 日志拦截器
     */
    private Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(log::info);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    /**
     * 自定义错误处理拦截器（增强可读性）
     */
    private Interceptor customErrorInterceptor() {
        return chain -> {
            Request request = chain.request();
            try {
                Response response = chain.proceed(request);
                if (!response.isSuccessful()) {
                    log.warn("请求失败: {} - {}", response.code(), response.message());
                }
                return response;
            } catch (Exception e) {
                log.error("请求异常: URL={} {}", request.url(), e.getMessage(), e);
                throw e;
            }
        };
    }

    /**
     * 信任所有证书（仅开发环境使用，生产环境建议使用 CA 证书）
     */
    private SSLSocketFactory trustAllSslSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{trustAllManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private X509TrustManager trustAllManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
        };
    }
}
