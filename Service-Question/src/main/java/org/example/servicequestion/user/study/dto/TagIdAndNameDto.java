/**
 * File Name: TagIdAndNameDto.java
 * Description: 标签ID和名称的数据传输对象
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * 用于在API接口中传输标签的基本信息(ID和名称)
 */
package org.example.servicequestion.user.study.dto;

import lombok.Data;

/**
 * 标签ID和名称的数据传输对象
 * 用于简化标签信息的传输,只包含ID和名称
 */
@Data
public class TagIdAndNameDto {
    /** 标签ID */
    private Long id;
    
    /** 标签名称 */
    private String name;
}
