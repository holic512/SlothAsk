/**
 * File Name: MailCodeMessage.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.dto;

import lombok.Data;

@Data
public class MailCodeMessage {
    private String email;
    private String username;
    private String purpose;
    private String verificationCode;

    public MailCodeMessage(String email, String username, String purpose, String verificationCode) {
        this.email = email;
        this.username = username;
        this.purpose = purpose;
        this.verificationCode = verificationCode;
    }

    // Getter 和 Setter 省略（可以使用 Lombok @Data 简化）
}
