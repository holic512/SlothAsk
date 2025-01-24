package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.enums.PostUserAdminEnum;
import org.example.serviceuser.admin.request.AddUserRequest;

public interface PostUserAdminService {
    PostUserAdminEnum addUser(AddUserRequest addUserRequest);
}
