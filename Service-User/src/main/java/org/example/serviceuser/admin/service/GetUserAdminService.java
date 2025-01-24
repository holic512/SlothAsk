/**
 * File Name: GetAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.service;

import org.example.serviceuser.admin.dto.PageDto;

public interface GetUserAdminService {

    /**
     * 获取用户列表
     * @param keyword 搜索关键词（用户名/邮箱/手机号）
     * @param status 用户状态
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @return 用户列表
     */
    PageDto getUsers(String keyword, Integer status, int pageNum, int pageSize);

}
