/**
 * File Name: MQCMailService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.service;

public interface MQCMailService {
    boolean sendVerificationEmail(String email,
                               String username,
                               String purpose,
                               String verificationCode);
}
