/**
 * File Name: PostAdminController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-23
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.controller;

import org.example.serviceuser.admin.dto.ResultDto;
import org.example.serviceuser.admin.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class PostAdminController {

    @PostMapping("/add")
    public ResultDto addUser(@RequestBody UserDto userDto) {

        return null;
    }
}
