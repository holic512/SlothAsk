package org.example.servicegateway.requestlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.servicegateway.requestlog.entity.UserRequestLog;

import java.util.List;

@Mapper
public interface UserRequestLogMapper {

    @Insert("<script>INSERT INTO user_request_log (user_id, request_time, request_path) VALUES " +
            "<foreach collection='list' item='log' separator=','>" +
            "(#{log.userId}, #{log.requestTime}, #{log.requestPath})" +
            "</foreach></script>")
    void insertUserRequestLogs(@Param("list") List<UserRequestLog> userRequestLogs);

}
