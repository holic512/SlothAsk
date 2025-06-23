/**
 * File Name: UserSubmitCountDto.java
 * Description: 用户提交次数统计DTO
 * Author: holic512
 * Created Date: 2025-06-23
 * Version: 1.0
 * Usage:
 * 用于返回用户每日提交次数统计数据
 */
package org.example.servicequestion.user.study.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubmitCountDto {
    
    /**
     * 提交日期 (格式: yyyy-MM-dd)
     */
    @JsonProperty("date")
    private String date;
    
    /**
     * 当日提交次数
     */
    @JsonProperty("count")
    private Integer count;
}