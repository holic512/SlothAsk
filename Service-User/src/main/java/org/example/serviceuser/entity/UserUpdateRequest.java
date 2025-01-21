/**
 * File Name: UserUpdateRequest.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.entity;

import lombok.Data;
import org.example.serviceuser.dto.UserDto;
import org.example.serviceuser.dto.UserProfileDto;

@Data
public class UserUpdateRequest {
    private UserDto userDto;
    private UserProfileDto userProfileDto;


}
