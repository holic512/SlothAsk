/**
 * File Name: UserHeaderGlobalFilter.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-13
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicegateway.config.Filter;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import org.example.servicegateway.util.StpKit;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserHeaderGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        try {
            SaReactorSyncHolder.setContext(exchange);
            // 用户登录
            if (StpKit.USER.isLogin()) {
                String userId = StpKit.USER.getLoginIdAsString();

                System.out.println("用户ID:" + userId + "发起请求");


                // 插入用户的 id 和 UpcId
                ServerHttpRequest modifiedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", userId)
                        .header("X-Upc-Id", StpKit.USER.getSession().getString("userUpcId"))
                        .build();
                exchange = exchange.mutate().request(modifiedRequest).build();
            } else if (StpKit.ADMIN.isLogin()) {
                String AdminId = StpKit.ADMIN.getLoginIdAsString();

                System.out.println("管理员ID:" + AdminId + "发起请求");


                ServerHttpRequest modifiedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-Admin-Id", AdminId)
                        .build();
                exchange = exchange.mutate().request(modifiedRequest).build();
            }
        } finally {
            SaReactorSyncHolder.clearContext();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 确保在其 全局过滤器之后进行
    }
}

