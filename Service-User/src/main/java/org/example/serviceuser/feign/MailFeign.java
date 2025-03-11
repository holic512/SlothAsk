/**
 * File Name: MailFeign.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-notification")
public interface MailFeign {

    @PostMapping("/mail/sendVerificationEmail")
    void sendVerificationEmail(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String purpose,
            @RequestParam String verificationCode
    );

}
