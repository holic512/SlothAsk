package org.example.serviceuser.user.sign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.example.serviceuser.entity.UserProfile;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}
