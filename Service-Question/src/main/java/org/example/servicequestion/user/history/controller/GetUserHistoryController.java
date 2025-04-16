/**
 * File Name: GetUserHistoryController.java
 * Description: 获取用户历史记录控制器
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.controller;

import java.util.List;

import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.history.dto.HistoryQuestionDTO;
import org.example.servicequestion.user.history.dto.TagSimpleDTO;
import org.example.servicequestion.user.history.dto.UserHistoryDTO;
import org.example.servicequestion.user.history.service.GetUserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/history")
public class GetUserHistoryController {

    private final GetUserHistoryService getUserHistoryService;

    @Autowired
    public GetUserHistoryController(GetUserHistoryService getUserHistoryService) {
        this.getUserHistoryService = getUserHistoryService;
    }

    /**
     * 获取用户的历史问题信息
     *
     * @param userId 用户ID
     * @param page   页码，默认为1
     * @return 用户历史问题信息列表
     */
    @GetMapping("/questions")
    public ApiResponse getUserHistoryQuestions(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        try {
            HistoryQuestionDTO historyList = getUserHistoryService.getUserHistory(userId, page, 15);
            return new ApiResponse(200, "获取用户历史记录成功", historyList);
        } catch (Exception e) {
            return new ApiResponse(400, "获取用户历史记录失败: " + e.getMessage());
        }
    }
}
