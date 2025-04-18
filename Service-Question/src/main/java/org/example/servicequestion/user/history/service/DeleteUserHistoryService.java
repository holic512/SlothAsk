/**
 * File Name: DeleteUserHistoryService.java
 * Description: 删除用户历史服务接口
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.service;

public interface DeleteUserHistoryService {

    /**
     * 删除单条历史记录
     *
     * @param userId   用户ID
     * @param questionId 问题ID
     * @return 是否成功
     */
    boolean deleteHistoryRecord(Long userId, String questionId);

    /**
     * 清空指定日期的历史记录
     *
     * @param userId 用户ID
     * @param date   日期字符串
     * @return 是否成功
     */
    boolean clearDayHistoryRecords(Long userId, String date);
}
