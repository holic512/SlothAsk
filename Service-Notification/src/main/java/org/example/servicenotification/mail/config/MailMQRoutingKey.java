/**
 * File Name: MailMQRoutingKey.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-11
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicenotification.mail.config;

public enum MailMQRoutingKey  {
    EMAIL_VERIFICATION_ROUTING_KEY("email.verification");

    private final String key;

    MailMQRoutingKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

