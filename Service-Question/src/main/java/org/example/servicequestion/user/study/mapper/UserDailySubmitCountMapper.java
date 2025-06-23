/**
 * File Name: UserDailySubmitCountMapper.java
 * Description: 用户每日提交次数数据访问层
 * Author: holic512
 * Created Date: 2025-06-23
 * Version: 1.0
 * Usage:
 * 提供用户每日提交次数相关的数据库操作
 */
package org.example.servicequestion.user.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.servicecommon.entity.UserDailySubmitCount;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserDailySubmitCountMapper extends BaseMapper<UserDailySubmitCount> {
    
    /**
     * 查询用户最近指定天数的提交记录
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 用户提交记录列表
     */
    @Select("SELECT * FROM user_daily_submit_count WHERE user_id = #{userId} " +
            "AND submit_date BETWEEN #{startDate} AND #{endDate} " +
            "ORDER BY submit_date DESC")
    List<UserDailySubmitCount> selectUserSubmitCountByDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}