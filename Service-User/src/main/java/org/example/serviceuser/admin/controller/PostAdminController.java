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

import jakarta.validation.Valid;
import org.example.serviceuser.admin.dto.ResultDto;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.enums.PostAdminEnum;
import org.example.serviceuser.admin.request.AddUserRequest;
import org.example.serviceuser.admin.service.PostAdminService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class PostAdminController {

    private final PostAdminService postAdminService;

    public PostAdminController(PostAdminService postAdminService) {
        this.postAdminService = postAdminService;
    }

    @PostMapping("/addUser")
    public ApiResponse addUser(@Valid @RequestBody AddUserRequest userRequest) {

        PostAdminEnum result = postAdminService.addUser(userRequest);

        if (result == PostAdminEnum.SUCCESS) {
            return new ApiResponse(200, "插入成功");
        } else {
            return new ApiResponse(403, result.getMessage());
        }

    }
}
