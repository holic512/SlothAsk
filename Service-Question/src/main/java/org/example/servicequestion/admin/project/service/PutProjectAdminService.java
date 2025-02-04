/**
 * File Name: PutProjectAdminService.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-02
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.project.service;

import org.example.servicequestion.admin.project.enums.PutProjectAdminEnum;
import org.example.servicequestion.admin.project.request.PutProjectAdminRequest;

public interface PutProjectAdminService {

    /**
     * 修改项目信息
     * @param request
     * @return
     */
    PutProjectAdminEnum modifyProject(PutProjectAdminRequest request);
}
