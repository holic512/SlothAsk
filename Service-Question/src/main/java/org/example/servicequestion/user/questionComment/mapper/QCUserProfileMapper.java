/**
 * File Name: QCUserProfileMapper.java
 * Description: 用户个人资料Mapper接口
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.servicequestion.entity.UserProfile;

@Mapper
public interface QCUserProfileMapper extends BaseMapper<UserProfile> {
} 