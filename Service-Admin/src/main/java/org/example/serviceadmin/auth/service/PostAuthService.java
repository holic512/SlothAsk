/**
 * File Name: PostAuthService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.auth.service;

import org.example.serviceadmin.auth.enmus.LoginEnum;

public interface PostAuthService {
    LoginEnum login(String username, String password);
}
