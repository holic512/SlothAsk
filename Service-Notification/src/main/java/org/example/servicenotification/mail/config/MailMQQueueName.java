/**
 * File Name: MailMQQueueName.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.config;

public enum MailMQQueueName {
    EMAIL_VERIFICATION_QUEUE("email_verification_queue");

    private final String value;

    MailMQQueueName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

