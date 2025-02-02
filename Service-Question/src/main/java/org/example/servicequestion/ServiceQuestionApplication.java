package org.example.servicequestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceQuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceQuestionApplication.class, args);
    }

}
