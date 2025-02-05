/**
 * File Name: AddProjectAdminRequest.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProjectAdminRequest {

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
