package org.example.servicegateway.requestlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.servicegateway.requestlog.entity.UserRequestLog;

import java.util.List;

@Mapper
public interface UserRequestLogMapper extends BaseMapper<UserRequestLog> {

    void insertUserRequestLogs(@Param("list") List<UserRequestLog> userRequestLogs);

}
