package org.example.servicegateway.requestlog.service;

import org.example.servicegateway.requestlog.entity.AdminRequestLog;
import org.example.servicegateway.requestlog.entity.UserRequestLog;

import java.util.List;

public interface RequestLogService {

    void saveAdminRequestLogs(List<AdminRequestLog> adminRequestLogs);
    void saveUserRequestLogs(List<UserRequestLog> userRequestLogs);
}
