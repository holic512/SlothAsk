/**
 * File Name: DeleteUserAdminService.java
 * Description: 用户管理系统-管理员删除用户服务接口
 * Author: lv
 * Created Date: 2025-01-24
 * Version: 1.0
 * Usage: 提供管理员删除用户的服务接口，包括单个删除和批量删除功能
 */
package org.example.serviceuser.admin.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface DeleteUserAdminService {
    /**
     * 删除单个用户
     * @param id 要删除的用户ID
     * @return 删除成功返回影响的行数，失败返回0
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUser(Long id);

    /**
     * 批量删除用户
     * @param ids 要删除的用户ID列表
     * @return 删除成功返回true，失败返回false
     */
    @Delete("<script>" +
            "DELETE FROM user WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    boolean deleteUsersBatch(@Param("ids") List<Long> ids);
}
