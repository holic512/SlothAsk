/**
 * File Name: GetAdminService.java
 * Description: 用户管理系统-管理员查询用户服务接口
 * Author: holic512
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage: 提供管理员查询用户列表的服务接口，支持分页、关键词搜索和状态筛选功能
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
