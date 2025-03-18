/**
 * File Name: GetUserCategoryController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-13
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.user.category.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/category")
public class GetUserCategoryController {
    @GetMapping("")
    public String test(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                       @RequestHeader(value = "X-Upc-Id", required = false) Long upcId) {
        return "当前用户ID：" + userId + "\n" + "用户所选项目Id:" + upcId;
    }
}
