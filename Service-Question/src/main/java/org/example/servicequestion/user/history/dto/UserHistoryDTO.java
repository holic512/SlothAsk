/**
 * File Name: UserHistoryDTO.java
 * Description: 用户历史问题信息DTO
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 */
package org.example.servicequestion.user.history.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserHistoryDTO {
    private String virtualId;      // 问题虚拟ID
    private String title;          // 问题标题
    private Integer difficulty;    // 难度
    private String categoryName;   // 分类名称
    private LocalDateTime visitTime; // 最近访问时间
    private String tagCategoryIds;

} 