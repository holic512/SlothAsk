/**
 * File Name: CategoryIdAndNameDto.java
 * Description: 分类ID和名称的数据传输对象
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * 用于在API接口中传输题库分类的基本信息(ID和名称)
 */
package org.example.servicequestion.user.study.dto;

import lombok.Data;

/**
 * 分类ID和名称的数据传输对象
 * 用于简化分类信息的传输,只包含ID和名称
 */
@Data
public class CategoryIdAndNameDto {
    /** 分类ID */
    Long id;
    /** 分类名称 */
    String name;
}
