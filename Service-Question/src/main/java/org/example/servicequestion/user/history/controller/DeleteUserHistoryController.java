/**
 * File Name: DeleteUserHistoryController.java
 * Description: 删除用户历史记录控制器
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.controller;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.history.service.DeleteUserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/history")
public class DeleteUserHistoryController {

    private final DeleteUserHistoryService deleteUserHistoryService;

    @Autowired
    public DeleteUserHistoryController(DeleteUserHistoryService deleteUserHistoryService) {
        this.deleteUserHistoryService = deleteUserHistoryService;
    }

    /**
     * 删除单条历史记录
     *
     * @param userId   用户ID
     * @param questionId 问题ID
     * @return 操作结果
     */
    @DeleteMapping("/record/{questionId}")
    public ApiResponse deleteHistoryRecord(
            @RequestHeader(value = "X-User-Id") Long userId,
            @PathVariable String questionId) {
        try {
            boolean success = deleteUserHistoryService.deleteHistoryRecord(userId, questionId);
            if (success) {
                return new ApiResponse(200, "删除历史记录成功", true);
            } else {
                return new ApiResponse(400, "删除历史记录失败", false);
            }
        } catch (Exception e) {
            return new ApiResponse(500, "删除历史记录错误: " + e.getMessage(), false);
        }
    }

    /**
     * 清空指定日期的历史记录
     *
     * @param userId 用户ID
     * @param date   日期字符串
     * @return 操作结果
     */
    @DeleteMapping("/day")
    public ApiResponse clearDayHistoryRecords(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam String date) {
        try {
            boolean success = deleteUserHistoryService.clearDayHistoryRecords(userId, date);
            if (success) {
                return new ApiResponse(200, "清空历史记录成功", true);
            } else {
                return new ApiResponse(400, "清空历史记录失败", false);
            }
        } catch (Exception e) {
            return new ApiResponse(500, "清空历史记录错误: " + e.getMessage(), false);
        }
    }
}
