package org.example.servicequestion.user.question.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.example.servicequestion.config.HistoryQueueConfig.HistoryRecord;
import org.example.servicequestion.user.question.mapper.UserQuestionHistoryMapper;
import org.example.servicequestion.user.question.service.QuestionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 问题历史记录服务实现类(优化版)
 * 使用Map+Queue混合结构实现内存级去重
 */
@Service
public class QuestionHistoryServiceImpl implements QuestionHistoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionHistoryServiceImpl.class);
    
    // 内存缓存去重阈值
    private static final int BATCH_THRESHOLD = 200;
    
    // 标记是否正在执行批量保存过程
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    
    // 队列，保存待处理的历史记录
    private final BlockingQueue<HistoryRecord> historyQueue;
    
    // Map结构，记录最近访问的记录，以userId:questionId为key
    private final Map<String, LocalDateTime> historyMap;
    
    private final UserQuestionHistoryMapper userQuestionHistoryMapper;
    
    @Autowired
    public QuestionHistoryServiceImpl(BlockingQueue<HistoryRecord> historyRecordQueue,
                                     UserQuestionHistoryMapper userQuestionHistoryMapper) {
        this.historyQueue = historyRecordQueue;
        this.userQuestionHistoryMapper = userQuestionHistoryMapper;
        this.historyMap = new ConcurrentHashMap<>();
    }
    
    /**
     * 生成历史记录的唯一标识
     */
    private String generateKey(Long userId, Long questionId) {
        return userId + ":" + questionId;
    }
    
    @Override
    public void addHistoryRecord(Long userId, Long questionId) {
        if (userId == null || questionId == null || userId <= 0) {
            return;
        }
        
        String recordKey = generateKey(userId, questionId);
        LocalDateTime now = LocalDateTime.now();
        
        // 添加/更新访问时间到Map
        historyMap.put(recordKey, now);
        
        // 添加到队列中等待批量处理
        try {
            historyQueue.offer(new HistoryRecord(userId, questionId));
            
            // 当达到阈值时，立即触发批量保存
            if (historyQueue.size() >= BATCH_THRESHOLD) {
                saveHistoryRecordBatch();
            }
        } catch (Exception e) {
            // 忽略异常，不记录日志
        }
    }
    
    /**
     * 每5秒执行一次批量保存
     */
    @Scheduled(fixedRate = 5000)
    @Override
    public void saveHistoryRecordBatch() {
        // 如果队列为空，直接返回
        if (historyQueue.isEmpty()) {
            return;
        }
        
        // 使用CAS操作确保同一时间只有一个线程执行批量保存
        // 如果已经有线程在处理，则直接返回
        if (!isProcessing.compareAndSet(false, true)) {
            logger.debug("批量保存操作正在进行中，跳过本次执行");
            return;
        }
        
        try {
            List<HistoryRecord> recordBatch = new ArrayList<>();
            historyQueue.drainTo(recordBatch);
            
            if (recordBatch.isEmpty()) {
                return;
            }
            
            // 创建一个临时Map存储最终要写入数据库的记录(去重)
            Map<String, HistoryRecord> uniqueRecords = new ConcurrentHashMap<>();
            
            // 构建唯一记录集合
            for (HistoryRecord record : recordBatch) {
                String key = generateKey(record.getUserId(), record.getQuestionId());
                // 从historyMap获取最新访问时间
                LocalDateTime visitTime = historyMap.get(key);
                // 如果不存在访问时间(罕见情况)，则使用当前时间
                if (visitTime == null) {
                    visitTime = LocalDateTime.now();
                }
                
                // 存入唯一记录Map，保证每个用户-问题组合只有一条最新记录
                uniqueRecords.put(key, record);
            }
            
            // 执行批量更新
            try {
                for (Map.Entry<String, HistoryRecord> entry : uniqueRecords.entrySet()) {
                    HistoryRecord record = entry.getValue();
                    LocalDateTime visitTime = historyMap.get(entry.getKey());
                    
                    if (visitTime == null) {
                        visitTime = LocalDateTime.now();
                    }
                    
                    // 使用INSERT ON DUPLICATE KEY UPDATE高效插入或更新记录
                    userQuestionHistoryMapper.insertOrUpdateVisitRecord(
                        record.getUserId(), record.getQuestionId(), visitTime);
                }
            } catch (Exception e) {
                logger.error("保存历史记录失败", e);
            }
        } finally {
            // 无论执行成功还是失败，都重置处理标志
            isProcessing.set(false);
        }
    }
    
    /**
     * 清理过期的内存缓存数据(每小时执行一次)
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupHistoryMap() {
        if (historyMap.size() > 5000) {
            // 删除24小时前的记录
            LocalDateTime threshold = LocalDateTime.now().minusHours(24);
            historyMap.entrySet().removeIf(entry -> entry.getValue().isBefore(threshold));
        }
    }
} 