package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.config.ApiResponse.ApiResponse;

public interface PostUserService {
    ApiResponse addUser(UserDto userDto);
}
