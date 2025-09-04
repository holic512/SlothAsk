/**
 * File Name: VipInfoDTO.java
 * Description: VIP信息数据传输对象
 * Author: holic512
 * Created Date: 2025-01-20
 * Version: 1.0
 */
package org.example.servicevip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VipInfoDTO {
    
    /**
     * VIP记录ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * VIP类型：1=月付，2=永久
     */
    private Integer vipType;
    
    /**
     * VIP开始时间（格式化为字符串）
     */
    private String startTime;
    
    /**
     * VIP到期时间（格式化为字符串）
     */
    private String endTime;
    
    /**
     * VIP是否生效
     */
    private Boolean isActive;
}