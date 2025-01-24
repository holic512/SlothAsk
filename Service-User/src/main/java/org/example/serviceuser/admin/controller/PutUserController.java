/**
 * File Name: PutAdminController.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.controller;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.service.PutUserService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class PutUserController {

    private final PutUserService putUserService;

    @Autowired
    public PutUserController(PutUserService putUserService) {
        this.putUserService = putUserService;
    }

    @PutMapping("/password")
    public ApiResponse UpdatePassword(@RequestParam("id")int id, @RequestParam("password")String password) {
        String newPassword = putUserService.UpdatePassword(id, password);

        if("failure".equals(newPassword)) {
            return new ApiResponse(500,"failure",null);
        }

        return new ApiResponse(200,"success",null);

    }


    @PutMapping("/update")
    public ApiResponse updateUser(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        return putUserService.updateUser(userDto);
    }
}
