/**
 * File Name: BatchDeleteRequest.java
 * Description: 批量删除用户请求数据传输对象
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage: 用于接收管理员批量删除用户时的请求数据，包含要删除的用户ID列表
 */
package org.example.serviceuser.admin.request;

import lombok.Data;

import java.util.List;
@Data
public class BatchDeleteRequest {
    private List<Long> ids;  // 用户 ID 列表
}
