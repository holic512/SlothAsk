/**
 * File Name: UserSignUpcMapper.java
 * Description: Todo 用于查询获取用户选择的项目内容
 * Author: holic512
 * Created Date: 2025-03-13
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.sign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.serviceuser.entity.UserProjectCategory;

@Mapper
public interface UserSignUpcMapper extends BaseMapper<UserProjectCategory> {

    @Select("select upc.project_id from user_project_category upc where user_id = #{uid} ")
    Long selectPidByUid(@Param("uid") Long uid);

}
