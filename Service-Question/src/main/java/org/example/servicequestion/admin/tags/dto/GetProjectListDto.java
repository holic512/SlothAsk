/**
 * File Name: GetProjectListDto.java
 * Description: 项目列表数据传输对象，用于封装项目基本信息
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   用于在获取项目列表接口中传输项目数据，包含以下字段：
 *   - id: 项目唯一标识
 *   - name: 项目名称
 *   - description: 项目描述
 * 
 * 该DTO主要用于：
 * 1. 项目列表展示
 * 2. 标签关联项目时的数据选择
 */
package org.example.servicequestion.admin.tags.dto;

import lombok.Data;

@Data
public class GetProjectListDto {
    Long id;
    String name;
    String description;
}
