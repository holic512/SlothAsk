/**
 * File Name: UserCaptchaService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.captcha;

import java.io.IOException;
import java.util.Map;

public interface UserCaptchaService {

    Map<String, Object> captcha() throws IOException;

    boolean validate(ValidateDto userDto);
}
