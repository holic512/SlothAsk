/**
 * File Name: AuthProvider.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.sign.enums;

import lombok.Getter;

@Getter
public enum AuthProvider {

    GITHUB("github"),
    WECHAT("wechat"),
    QQ("qq"),
    GOOGLE("google"),
    ALIPAY("alipay");

    private final String value;

    AuthProvider(String value) {
        this.value = value;
    }

    public static AuthProvider fromValue(String value) {
        for (AuthProvider provider : AuthProvider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unsupported provider: " + value);
    }
}
