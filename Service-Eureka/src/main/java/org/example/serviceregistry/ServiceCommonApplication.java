package org.example.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCommonApplication.class, args);
    }

}
