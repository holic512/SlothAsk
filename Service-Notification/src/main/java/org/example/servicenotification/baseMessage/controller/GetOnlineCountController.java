/**
 * File Name: GetOnlineCountController.java
 * Description: 在线人数统计控制器
 * Author: holic512
 * Created Date: 2025-06-26
 * Version: 1.0
 * Usage:
 * 提供获取系统全局在线人数的接口，汇总各节点的在线连接数，用于统计分布式SSE连接总量。
 */

package org.example.servicenotification.baseMessage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("baseMessage")
public class GetOnlineCountController {

    /**
     * 获取全局在线人数接口
     * GET /sse/online-count
     */
    @GetMapping("/sse/online-count")
    public Object getOnlineCount() {
        // TODO: 返回全局在线人数，当前先返回 null 占位
        return null;
    }
}
