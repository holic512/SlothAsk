package org.example.servicegateway.requestlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.servicegateway.requestlog.entity.AdminRequestLog;

import java.util.List;


@Mapper
public interface AdminRequestLogMapper {

    @Insert("<script>INSERT INTO admin_request_log (admin_id, request_time, request_path) VALUES " +
            "<foreach collection='list' item='log' separator=','>" +
            "(#{log.adminId}, #{log.requestTime}, #{log.requestPath})" +
            "</foreach></script>")
    void insertAdminRequestLogs(@Param("list") List<AdminRequestLog> adminRequestLogs);
}
