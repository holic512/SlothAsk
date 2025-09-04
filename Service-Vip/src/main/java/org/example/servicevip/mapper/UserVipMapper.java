/**
 * File Name: UserVipMapper.java
 * Description: 用户VIP信息数据访问层接口
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicevip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.servicecommon.entity.UserVip;

@Mapper
public interface UserVipMapper extends BaseMapper<UserVip> {
    
}