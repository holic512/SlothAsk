package org.example.serviceuser.service;

import org.example.serviceuser.dto.ResultDto;
import org.example.serviceuser.dto.UserDto;
import org.example.serviceuser.dto.UserProfileDto;

import java.util.List;

public interface UserService {

    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    String UpdatePassword(int id, String password);

    /**
     * 获取用户列表
     * @param keyword 搜索关键词（用户名/邮箱/手机号）
     * @param status 用户状态
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @return 用户列表
     */
    ResultDto getUsers(String keyword, Integer status, int pageNum, int pageSize);

    // 更新用户基本信息
    int updateUser(UserDto userDto);

    // 更新用户资料
    int updateUserProfile(UserProfileDto userProfileDto);

    int deleteUser(int id);

    int deleteUsersBatch(List<Integer> ids);
}
