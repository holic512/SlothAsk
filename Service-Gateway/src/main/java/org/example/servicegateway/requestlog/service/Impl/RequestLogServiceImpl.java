package org.example.servicegateway.requestlog.service.Impl;

import org.example.servicegateway.requestlog.entity.AdminRequestLog;
import org.example.servicegateway.requestlog.entity.UserRequestLog;
import org.example.servicegateway.requestlog.mapper.AdminRequestLogMapper;
import org.example.servicegateway.requestlog.mapper.UserRequestLogMapper;
import org.example.servicegateway.requestlog.service.RequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestLogServiceImpl implements RequestLogService {

    private final UserRequestLogMapper userRequestLogMapper;
    private final AdminRequestLogMapper adminRequestLogMapper;

    // 构造函数注入
    @Autowired
    public RequestLogServiceImpl(UserRequestLogMapper userRequestLogMapper,
                                 AdminRequestLogMapper adminRequestLogMapper) {
        this.userRequestLogMapper = userRequestLogMapper;
        this.adminRequestLogMapper = adminRequestLogMapper;
    }

    // 异步保存管理员请求日志
    @Override
    @Async
    public void saveAdminRequestLogs(List<AdminRequestLog> adminRequestLogs) {

            adminRequestLogMapper.insertAdminRequestLogs(adminRequestLogs);
    }

    // 异步保存用户请求日志
    @Override
    @Async
    public void saveUserRequestLogs(List<UserRequestLog> userRequestLogs) {

        userRequestLogMapper.insertUserRequestLogs(userRequestLogs);

    }


}
