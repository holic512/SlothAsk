/**
 * File Name: AddTagsRequest.java
 * Description: 添加标签的请求参数对象，用于封装创建新标签时的数据
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   用于标签添加接口的请求参数，包含以下字段：
 *   - name: 标签名称（必填）
 *   - description: 标签描述（可选）
 *   - project_id: 所属项目ID（必填）
 *   - sort_order: 排序顺序（必填）
 *   - status: 标签状态（必填）
 * 
 * 数据验证：
 * 1. name: 使用@NotBlank注解确保不为空
 * 2. project_id, sort_order, status: 使用@NotNull注解确保不为空
 */
package org.example.servicequestion.admin.tags.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTagsRequest {
    @NotBlank(message = "名称不能为空")
    String name;
    String description;

    @NotNull
    Long project_id;
    @NotNull
    Integer sort_order;
    @NotNull
    Integer status;
}
