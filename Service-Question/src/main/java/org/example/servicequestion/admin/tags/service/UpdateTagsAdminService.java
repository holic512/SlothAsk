/**
 * File Name: UpdateTagsAdminService.java
 * Description: 标签更新服务接口，定义标签更新相关的业务操作
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   提供标签更新相关的业务操作接口：
 *   1. 更新标签信息
 * 
 * 接口功能：
 * 1. 处理标签更新的业务逻辑
 * 2. 数据验证和转换
 * 3. 返回更新操作的结果状态
 */
package org.example.servicequestion.admin.tags.service;

import org.example.servicequestion.admin.tags.dto.UpdateTagDto;

public interface UpdateTagsAdminService {
    /**
     * 更新标签信息
     * @param updateTagDto 更新的标签信息
     * @return 是否更新成功
     */
    boolean updateTag(UpdateTagDto updateTagDto);
} 