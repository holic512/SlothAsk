package org.example.serviceuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.example.serviceuser.dto.UserProfileDto;
import org.example.serviceuser.entity.UserProfile;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfileDto> {

    @Update("UPDATE user_profile SET nickname = #{nickname},  gender = #{gender}, age = #{age}, avatar = #{avatar}, bio = #{bio} WHERE id = #{id}")
    int updateUser(UserProfileDto userProfileDto);

}
