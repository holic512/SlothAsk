/**
 * File Name: PutProjectAdminRequest.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-02-03
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PutProjectAdminRequest {
    @NotNull
    // 项目id
    Long id;

    @NotBlank(message = "项目名称不能为空")
    // 项目名称
    String projectName;

    // 项目描述
    String projectDescription;

    @NotNull(message = "权重不能为空")
    Integer sort;
    @NotNull(message = "状态不能为空")
    Integer status;
}
