/**
 * File Name: JobItemMapper.java
 * Description: 岗位信息数据访问层接口
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 提供岗位信息的数据库操作接口，继承MyBatis-Plus的BaseMapper
 */
package org.example.servicejobrecruitpage.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.servicecommon.entity.JobItem;

@Mapper
public interface JobItemMapper extends BaseMapper<JobItem> {
}