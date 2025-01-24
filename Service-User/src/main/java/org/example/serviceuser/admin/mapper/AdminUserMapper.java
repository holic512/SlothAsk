package org.example.serviceuser.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.entity.User;

import java.util.List;

@Mapper
public interface AdminUserMapper extends BaseMapper<User> {


    /**
     * 获取用户列表，支持分页、搜索和状态过滤
     *
     * @param keyword  搜索关键词 (用户名/邮箱/手机号)
     * @param status   用户状态（可选）：1 - 正常，0 - 禁用
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     * @return 用户列表
     */
    @Select("SELECT u.id, u.username, p.nickname, u.email, u.phone, u.status, \n" +
            "       p.gender, p.age, p.avatar, p.bio, u.create_time as createTime \n" +
            "FROM user u \n" +
            "LEFT JOIN user_profile p ON u.id = p.user_id \n" +
            "WHERE \n" +
            "    (u.username LIKE CONCAT('%', #{keyword}, '%') \n" +
            "    OR u.email LIKE CONCAT('%', #{keyword}, '%') \n" +
            "    OR u.phone LIKE CONCAT('%', #{keyword}, '%')) \n" +
            "    AND u.status = #{status}\n" +
            "LIMIT #{pageNum}, #{pageSize};\n")
    List<UserDto> getUserListByStatus(@Param("keyword") String keyword,
                                      @Param("status") Integer status,
                                      @Param("pageNum") int pageNum,
                                      @Param("pageSize") int pageSize);


    /**
     * 获取用户列表，支持分页、搜索 -- 默认状态
     *
     * @param keyword  搜索关键词 (用户名/邮箱/手机号)
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     * @return 用户列表
     */
    @Select("SELECT u.id, u.username, p.nickname, u.email, u.phone, u.status, \n" +
            "       p.gender, p.age, p.avatar, p.bio, u.create_time as createTime \n" +
            "FROM user u \n" +
            "LEFT JOIN user_profile p ON u.id = p.user_id \n" +
            "WHERE \n" +
            "    (u.username LIKE CONCAT('%', #{keyword}, '%') \n" +
            "    OR u.email LIKE CONCAT('%', #{keyword}, '%') \n" +
            "    OR u.phone LIKE CONCAT('%', #{keyword}, '%')) \n" +
            "LIMIT #{pageNum}, #{pageSize};\n")
    List<UserDto> getUserList(@Param("keyword") String keyword,
                              @Param("pageNum") int pageNum,
                              @Param("pageSize") int pageSize);

    /**
     * 获取满足条件的用户总数
     *
     * @param keyword 搜索关键词（用户名/邮箱/手机号）
     * @param status  用户状态
     * @return 用户总数
     */
    @Select("SELECT COUNT(1)\n" +
            "FROM user u\n" +
            "LEFT JOIN user_profile p ON u.id = p.user_id\n" +
            "WHERE 1 = 1\n" +
            "  AND (\n" +
            "      u.username LIKE CONCAT('%', #{keyword}, '%')\n" +
            "      OR u.email LIKE CONCAT('%', #{keyword}, '%')\n" +
            "      OR u.phone LIKE CONCAT('%', #{keyword}, '%')\n" +
            "  )\n" +
            "  AND u.status = #{status};\n")
    int countUsersByStatus(String keyword, Integer status);

    /**
     * 获取满足条件的用户总数
     *
     * @param keyword 搜索关键词（用户名/邮箱/手机号）
     * @return 用户总数
     */
    @Select("SELECT COUNT(1)\n" +
            "FROM user u\n" +
            "LEFT JOIN user_profile p ON u.id = p.user_id\n" +
            "WHERE 1 = 1\n" +
            "  AND (\n" +
            "      u.username LIKE CONCAT('%', #{keyword}, '%')\n" +
            "      OR u.email LIKE CONCAT('%', #{keyword}, '%')\n" +
            "      OR u.phone LIKE CONCAT('%', #{keyword}, '%')\n" +
            "  )\n")
    int countUsers(String keyword);



    /**
     * 查询是否已有相同用户名的用户
     *
     * @param username 用户名
     * @return 用户名重复的数量
     */
    @Select("SELECT COUNT(*) FROM user u WHERE u.username = #{username}")
    Long countUsersByUsername(String username);


    /**
     * 查询是否已有相同邮箱的用户
     *
     * @param email 邮箱
     * @return 邮箱重复的数量
     */
    @Select("SELECT COUNT(*) FROM user u WHERE u.email = #{email}")
    Long countUsersByEmail(String email);


    /**
     * 查询是否已有相同手机号的用户
     *
     * @param phone 手机号
     * @return 手机号重复的数量
     */
    @Select("SELECT COUNT(*) FROM user u WHERE u.phone = #{phone}")
    Long countUsersByPhone(String phone);




    /**
     * 根据用户名更新用户密码
     *
     * @param id 用户id
     * @param newPassword 新密码
     * @return 更新的记录数
     */
    @Update("UPDATE user SET password = #{newPassword} WHERE id = #{id}")
    int updatePasswordByUserId(Long id, String newPassword);


    @Update("UPDATE user SET email = #{email},  phone = #{phone}, status = #{status} WHERE id = #{id}")
    int updateUser(UserDto userDto);

    // 删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUser(Long id);

    // 批量删除用户
    @Delete("<script>" +
            "DELETE FROM user WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteUsersBatch(@Param("ids") List<Long> ids);
}
