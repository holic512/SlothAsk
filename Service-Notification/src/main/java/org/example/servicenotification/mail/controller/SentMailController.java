/**
 * File Name: SentMailController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-10
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.controller;


import org.example.servicenotification.mail.service.MQSMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mail")
public class SentMailController {

    private final MQSMailService mqsMailService;

    @Autowired
    public SentMailController(MQSMailService mqsMailService) {
        this.mqsMailService = mqsMailService;
    }

    @PostMapping("sendVerificationEmail")
    public void sendVerificationEmail(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String purpose,
            @RequestParam String verificationCode
    ) {
        mqsMailService.sendVerificationEmail(email, username, purpose, verificationCode);
    }

}
