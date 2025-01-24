/**
 * File Name: PostAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.enums.PostAdminEnum;
import org.example.serviceuser.admin.request.AddUserRequest;

public interface PostAdminService {

    PostAdminEnum addUser(AddUserRequest addUserRequest);
}
