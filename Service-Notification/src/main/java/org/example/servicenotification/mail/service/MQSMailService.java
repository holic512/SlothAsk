/**
 * File Name: SentMailService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-10
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.service;

public interface MQSMailService {
    void sendVerificationEmail(String email,
                               String username,
                               String purpose,
                               String verificationCode);
}
