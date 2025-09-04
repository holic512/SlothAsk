package org.example.servicejobrecruitpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceJobRecruitPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceJobRecruitPageApplication.class, args);
    }

}
