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

    @Insert("INSERT INTO user_profile (user_id, nickname, avatar, gender, age, bio, create_time, update_time) " +
            "VALUES (#{userId}, #{nickname}, #{avatar}, #{gender}, #{age}, #{bio}, #{createTime}, #{updateTime})")
    int insert(UserProfile userProfile);

    @Update("UPDATE user_profile SET nickname = #{nickname}, gender = #{gender}, age = #{age}, avatar = #{avatar}, bio = #{bio} WHERE user_id = #{id}")
    int updateUserProfile(UserDto userDto);
}
