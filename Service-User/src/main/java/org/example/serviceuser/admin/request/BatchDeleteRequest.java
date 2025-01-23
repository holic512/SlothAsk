/**
 * File Name: BatchDeleteRequest.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.admin.request;

import lombok.Data;

import java.util.List;
@Data
public class BatchDeleteRequest {
    private List<Integer> ids;  // 用户 ID 列表
}
