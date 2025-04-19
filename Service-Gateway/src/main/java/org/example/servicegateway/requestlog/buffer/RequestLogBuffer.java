package org.example.servicegateway.requestlog.buffer;

import org.example.servicegateway.requestlog.entity.AdminRequestLog;
import org.example.servicegateway.requestlog.entity.UserRequestLog;
import org.example.servicegateway.requestlog.service.RequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

// 基于定时器和线程安全队列的异步批量写入
@Component
public class RequestLogBuffer {

    // 批量写入的日志数量
    private static final int BATCH_SIZE = 300;

    private final Queue<UserRequestLog> userLogQueue = new ConcurrentLinkedQueue<>();
    private final Queue<AdminRequestLog> adminLogQueue = new ConcurrentLinkedQueue<>();

    private final RequestLogService requestLogService;

    @Autowired
    public RequestLogBuffer(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    // 添加日志
    public void addUserLog(UserRequestLog log) {
        userLogQueue.add(log);
    }

    public void addAdminLog(AdminRequestLog log) {
        adminLogQueue.add(log);
    }

    // 定时刷新
    @Scheduled(fixedDelay = 5000) // 每 5 秒执行一次
    public void flushLogs() {
        flushUserLogs();
        flushAdminLogs();
    }

    private void flushUserLogs() {
        List<UserRequestLog> batch = new ArrayList<>();
        while (batch.size() < BATCH_SIZE && !userLogQueue.isEmpty()) {
            UserRequestLog log = userLogQueue.poll();
            if (log != null) batch.add(log);
        }
        if (!batch.isEmpty()) {
            requestLogService.saveUserRequestLogs(batch); // 批量入库
        }
    }

    private void flushAdminLogs() {
        List<AdminRequestLog> batch = new ArrayList<>();
        while (batch.size() < BATCH_SIZE && !adminLogQueue.isEmpty()) {
            AdminRequestLog log = adminLogQueue.poll();
            if (log != null) batch.add(log);
        }
        if (!batch.isEmpty()) {
            requestLogService.saveAdminRequestLogs(batch); // 批量入库
        }
    }
}
