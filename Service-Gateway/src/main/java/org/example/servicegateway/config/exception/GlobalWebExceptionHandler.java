package org.example.servicegateway.config.exception;

import org.example.servicegateway.config.ApiResponse.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.dev33.satoken.exception.NotLoginException;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class GlobalWebExceptionHandler implements ErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponse apiResponse = buildErrorResponse(ex, response);
        return writeResponse(response, apiResponse);
    }

    /**
     * 构建错误响应
     *
     * @param ex 异常
     * @param response HTTP响应对象
     * @return API响应对象
     */
    private ApiResponse buildErrorResponse(Throwable ex, ServerHttpResponse response) {
        // 获取真实异常
        Throwable cause = ex.getCause();

        if (cause instanceof NotLoginException) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return buildNotLoginResponse((NotLoginException) cause);
        } else {
            log.error("系统异常", ex);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ApiResponse(500, "服务器内部错误", null);
        }
    }

    /**
     * 构建未登录异常的响应
     *
     * @param ex NotLoginException异常
     * @return API响应对象
     */
    private ApiResponse buildNotLoginResponse(NotLoginException ex) {
        String message = getNotLoginMessage(ex.getType());
        return new ApiResponse(401, message, null);
    }

    /**
     * 获取未登录异常的具体信息
     *
     * @param type 未登录异常类型
     * @return 错误信息
     */
    private String getNotLoginMessage(String type) {
        switch (type) {
            case NotLoginException.NOT_TOKEN:
                return "未提供token";
            case NotLoginException.INVALID_TOKEN:
                return "token无效";
            case NotLoginException.TOKEN_TIMEOUT:
                return "token已过期";
            case NotLoginException.BE_REPLACED:
                return "token已被顶下线";
            case NotLoginException.KICK_OUT:
                return "token已被踢下线";
            default:
                return "未登录";
        }
    }

    /**
     * 将响应写入到HTTP响应中
     *
     * @param response HTTP响应对象
     * @param apiResponse API响应对象
     * @return Mono<Void>
     */
    private Mono<Void> writeResponse(ServerHttpResponse response, ApiResponse apiResponse) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(apiResponse);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("响应写入异常", e);
            return Mono.error(e);
        }
    }
} 