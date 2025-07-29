/**
 * File Name: BaseMessagePageResultDto.java
 * Description: 基础消息分页查询结果DTO
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 */
package org.example.serviceuser.user.basemessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessagePageResultDto {
    
    // 当前页码
    private Integer pageNum;
    
    // 每页大小
    private Integer pageSize;
    
    // 总记录数
    private Long total;
    
    // 总页数
    private Integer totalPages;
    
    // 当前页数据列表
    private List<BaseMessageResponseDto> records;
    
    // 是否有下一页
    private Boolean hasNext;
    
    // 是否有上一页
    private Boolean hasPrevious;
    
    /**
     * 构造方法，自动计算总页数和分页状态
     */
    public BaseMessagePageResultDto(Integer pageNum, Integer pageSize, Long total, List<BaseMessageResponseDto> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.hasNext = pageNum < totalPages;
        this.hasPrevious = pageNum > 1;
    }
}