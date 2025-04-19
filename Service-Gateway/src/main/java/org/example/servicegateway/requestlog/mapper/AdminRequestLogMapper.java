package org.example.servicegateway.requestlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.servicegateway.requestlog.entity.AdminRequestLog;

import java.util.List;


@Mapper
public interface AdminRequestLogMapper extends BaseMapper<AdminRequestLog> {
    void insertAdminRequestLogs(@Param("list") List<AdminRequestLog> adminRequestLogs);
}
