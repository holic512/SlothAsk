/**
 * File Name: UpdateTagDto.java
 * Description: 标签更新数据传输对象，用于封装标签更新请求的数据
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   用于标签更新接口的请求参数，包含以下字段：
 *   - id: 待更新的标签ID
 *   - projectId: 更新后的所属项目ID
 *   - name: 更新后的标签名称
 *   - description: 更新后的标签描述
 *   - sortOrder: 更新后的排序顺序
 *   - status: 更新后的标签状态
 * 
 * 该DTO主要用于：
 * 1. 标签信息的更新操作
 * 2. 验证更新请求的数据完整性
 */
package org.example.servicequestion.admin.tags.dto;

import lombok.Data;

@Data
public class UpdateTagDto {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private Integer sortOrder;
    private Integer status;
} 