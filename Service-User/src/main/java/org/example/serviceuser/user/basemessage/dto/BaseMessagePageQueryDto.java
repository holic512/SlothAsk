/**
 * File Name: BaseMessagePageQueryDto.java
 * Description: 基础消息分页查询请求DTO
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 */
package org.example.serviceuser.user.basemessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessagePageQueryDto {
    
    // 页码，默认第1页
    private Integer pageNum = 1;
    
    // 每页大小，默认10条
    private Integer pageSize = 10;
    
    // 消息类型过滤（可选）
    private Integer type;
    
    // 阅读状态过滤（可选）：0-未读，1-已读
    private Integer readStatus;
    
    // 创建时间范围查询 - 开始时间（可选）
    private LocalDateTime createTimeStart;
    
    // 创建时间范围查询 - 结束时间（可选）
    private LocalDateTime createTimeEnd;
    
    // 关键词搜索（在messageData中搜索，可选）
    private String keyword;
    
    // 排序字段，默认按创建时间排序
    private String orderBy = "created_time";
    
    // 排序方向：asc-升序，desc-降序，默认降序
    private String orderDirection = "desc";
}