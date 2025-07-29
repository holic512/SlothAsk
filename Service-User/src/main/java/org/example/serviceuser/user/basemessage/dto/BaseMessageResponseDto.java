/**
 * File Name: BaseMessageResponseDto.java
 * Description: 基础消息响应DTO，用于控制返回给前端的字段
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
public class BaseMessageResponseDto {
    
    // 主键ID
    private Long id;

    // 消息类型（业务中定义的枚举值）
    private Integer type;
    
    // 阅读状态：0-未读，1-已读
    private Integer readStatus;
    
    // 序列化后的消息内容（JSON字符串）
    private String messageData;
    
    // 记录创建时间
    private LocalDateTime createTime;
    
    // 注意：不包含updateTime字段
}