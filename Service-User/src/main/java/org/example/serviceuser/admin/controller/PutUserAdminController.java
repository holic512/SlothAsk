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

import jakarta.validation.Valid;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.enums.PutUserAdminEnum;
import org.example.serviceuser.admin.request.UpdatePasswordRequest;
import org.example.serviceuser.admin.service.PutUserAdminService;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class PutUserAdminController {

    private final PutUserAdminService putUserAdminService;

    @Autowired
    public PutUserAdminController(PutUserAdminService putUserAdminService) {
        this.putUserAdminService = putUserAdminService;
    }

    @PutMapping("/UpdatePassword")
    public ApiResponse UpdatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        PutUserAdminEnum result = putUserAdminService.UpdatePassword(updatePasswordRequest.getId(), updatePasswordRequest.getNewPassword());
        if(result == PutUserAdminEnum.SUCCESS) {
            return new ApiResponse(200,"更新密码成功");
        }else {
            return new ApiResponse(500,"更新密码失败");
        }
    }


    @PutMapping("/update")
    public ApiResponse updateUser(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        return putUserAdminService.updateUser(userDto);
    }
}
