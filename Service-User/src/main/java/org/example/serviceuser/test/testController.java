/**
 * File Name: testController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-06
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/users/hello")
    public String hello() {
        return "Hello from User Service!";
    }
}
