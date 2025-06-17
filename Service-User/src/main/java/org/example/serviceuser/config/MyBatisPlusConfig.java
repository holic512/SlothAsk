/**
 * File Name: MyBatisPlusConfig.java
 * Description: MyBatis-Plus分页插件配置类
 * Author: holic512
 * Created Date: 2025-03-05
 * Version: 1.0
 * Usage:
 * 配置MyBatis-Plus分页拦截器，为系统提供高效的分页查询功能，支持用户个人资料、评论和收藏问题的分页展示
 */
package org.example.serviceuser.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

}
