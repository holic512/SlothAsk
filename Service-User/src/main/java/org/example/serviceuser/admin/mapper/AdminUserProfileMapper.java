package org.example.serviceuser.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.example.serviceuser.admin.dto.UserDto;
import org.example.serviceuser.admin.dto.UserProfileDto;
import org.example.serviceuser.entity.UserProfile;

@Mapper
public interface AdminUserProfileMapper extends BaseMapper<UserProfile> {

    @Update("UPDATE user_profile SET nickname = #{nickname}, gender = #{gender}, age = #{age}, avatar = #{avatar}, bio = #{bio} WHERE user_id = #{userId}")
    int updateByUserId(UserProfile userProfile);
}
