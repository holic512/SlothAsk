package org.example.servicegateway.requestlog.buffer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.servicegateway.requestlog.entity.AdminRequestLog;
import org.example.servicegateway.requestlog.entity.UserRequestLog;
import org.example.servicegateway.requestlog.service.RequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 日志缓冲器：基于线程池 + 阻塞队列实现的异步、批量日志写入机制
 * 支持用户日志与管理员日志分开处理，提升写入效率与系统性能
 */
@Component
public class RequestLogBuffer {

    // 每批次写入日志的最大数量
    private static final int BATCH_SIZE = 300;

    // 阻塞队列：线程安全，用于缓冲日志写入请求
    private final BlockingQueue<UserRequestLog> userLogQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<AdminRequestLog> adminLogQueue = new LinkedBlockingQueue<>();

    private final RequestLogService requestLogService;

    // 使用线程池处理日志消费任务（分别用于用户与管理员日志）
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Autowired
    public RequestLogBuffer(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    /**
     * 添加用户请求日志（由外部调用线程调用，写入队列）
     */
    public void addUserLog(UserRequestLog log) {
        userLogQueue.offer(log); // offer 非阻塞添加，队列满时直接返回 false
    }

    /**
     * 添加管理员请求日志（由外部调用线程调用，写入队列）
     */
    public void addAdminLog(AdminRequestLog log) {
        adminLogQueue.offer(log);
    }

    /**
     * 在 Spring 初始化后启动消费者线程，分别消费用户日志和管理员日志
     */
    @PostConstruct
    public void initConsumers() {
        executorService.submit(this::consumeUserLogs);
        executorService.submit(this::consumeAdminLogs);
    }

    /**
     * 消费用户请求日志：阻塞读取 + 批量写入数据库
     */
    private void consumeUserLogs() {
        List<UserRequestLog> batch = new ArrayList<>(BATCH_SIZE);
        try {
            while (true) {
                // 队列为空时会等待，直到有新日志
                UserRequestLog log = userLogQueue.take();
                batch.add(log);

                // 批量拉取剩余日志（最多拉 BATCH_SIZE - 1 条）
                userLogQueue.drainTo(batch, BATCH_SIZE - 1);

                // 如果有日志则批量写入数据库
                if (!batch.isEmpty()) {
                    System.out.println("Processing batch of user logs...");
                    requestLogService.saveUserRequestLogs(new ArrayList<>(batch));
                    batch.clear();
                }
            }
        } catch (InterruptedException e) {
            // 如果线程被中断，退出消费循环
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 消费管理员请求日志
     */
    private void consumeAdminLogs() {
        List<AdminRequestLog> batch = new ArrayList<>(BATCH_SIZE);
        try {
            while (true) {
                AdminRequestLog log = adminLogQueue.take();
                batch.add(log);
                adminLogQueue.drainTo(batch, BATCH_SIZE - 1);

                if (!batch.isEmpty()) {
                    requestLogService.saveAdminRequestLogs(new ArrayList<>(batch));
                    batch.clear();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 项目关闭时关闭线程池，防止数据丢失
     */
    @PreDestroy
    public void shutdown() {
        executorService.shutdown(); // 拒绝新任务
        try {
            // 等待已有任务执行完，最多等待 10 秒
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // 强制终止
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
