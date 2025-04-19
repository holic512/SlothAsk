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

import org.example.servicegateway.requestlog.buffer.RequestLogBuffer;
import org.example.servicegateway.requestlog.entity.AdminRequestLog;
import org.example.servicegateway.requestlog.entity.UserRequestLog;
import org.example.servicegateway.util.StpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
@Component
public class UserHeaderGlobalFilter implements GlobalFilter, Ordered {

    private final RequestLogBuffer requestLogBuffer;

    @Autowired
    public UserHeaderGlobalFilter( RequestLogBuffer requestLogBuffer) {

        this.requestLogBuffer = requestLogBuffer;
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {


        // 用户登录
        if (StpKit.USER.isLogin()) {
            String userId = StpKit.USER.getLoginIdAsString();
            String requestPath = exchange.getRequest().getURI().getPath();
            LocalDateTime requestTime= LocalDateTime.now();

            // 记录请求信息到数据库
            UserRequestLog userRequestLog = new UserRequestLog();
            userRequestLog.setUserId(Long.parseLong(userId));
            userRequestLog.setRequestTime(requestTime);
            userRequestLog.setRequestPath(requestPath);
            requestLogBuffer.addUserLog(userRequestLog); // 保存用户请求日志
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
            String requestPath = exchange.getRequest().getURI().getPath();
            LocalDateTime requestTime= LocalDateTime.now();

            // 记录请求信息到数据库
            AdminRequestLog adminRequestLog = new AdminRequestLog();
            adminRequestLog.setAdminId(Long.parseLong(AdminId));
            adminRequestLog.setRequestTime(requestTime);
            adminRequestLog.setRequestPath(requestPath);
            requestLogBuffer.addAdminLog(adminRequestLog);
            System.out.println("管理员ID:" + AdminId + "发起请求");


            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Admin-Id", AdminId)
                    .build();
            exchange = exchange.mutate().request(modifiedRequest).build();
        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 确保在其它过滤器之前执行
    }
}

