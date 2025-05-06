/**
 * File Name: SaTokenConfigure.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicegateway.config.satoken;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import org.example.servicegateway.util.StpKit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SaTokenConfigure {

    /**
     * 注册 [Sa-Token 全局过滤器]，适用于 WebFlux
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定拦截路由与放行路由
                .addInclude("/**")
                .addExclude("/favicon.ico")

                // 认证函数：每次请求执行
                .setAuth(obj -> {
                    // 放行无需认证的接口
                    SaRouter.notMatch("/service-question/**", "/service-admin/auth/**", "/service-admin/captcha/**", "service-ai/**");
                    // 拦截管理员接口 和 各个有关于管理员操作的子服务
                    SaRouter.match("/service-admin/**", r -> StpKit.ADMIN.checkLogin());
                    SaRouter.match("/service-user/admin/**", r -> StpKit.ADMIN.checkLogin());
                    SaRouter.match("/service-user/users/**", r -> StpKit.ADMIN.checkLogin());
                });
    }
}
