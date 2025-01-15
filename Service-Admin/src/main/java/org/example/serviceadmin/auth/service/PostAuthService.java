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

import cn.dev33.satoken.stp.SaTokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.example.serviceadmin.auth.enmus.LoginEnum;


public interface PostAuthService {
    Pair<LoginEnum, SaTokenInfo> login(String username, String password, HttpServletRequest request);
}
