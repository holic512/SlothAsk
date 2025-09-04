package org.example.servicevip;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.servicevip.mapper")
public class ServiceVipApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceVipApplication.class, args);
    }

}
