/**
 * File Name: GetUserHistoryService.java
 * Description: 获取用户历史服务接口
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.service;

import java.util.List;

import org.example.servicequestion.user.history.dto.HistoryQuestionDTO;
import org.example.servicequestion.user.history.dto.TagSimpleDTO;
import org.example.servicequestion.user.history.dto.UserHistoryDTO;
import org.springframework.data.util.Pair;

public interface GetUserHistoryService {
    /**
     * 获取用户的历史问题信息
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 用户历史问题信息列表
     */
    HistoryQuestionDTO getUserHistory(Long userId, int page, int pageSize);
}
