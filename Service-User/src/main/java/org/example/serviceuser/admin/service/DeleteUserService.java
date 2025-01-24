package org.example.serviceuser.admin.service;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeleteUserService {
    // 删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUser(Long id);

    @Delete("<script>" +
            "DELETE FROM user WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    boolean deleteUsersBatch(@Param("ids") List<Long> ids);

}
