/**
 * File Name: DeleteTagsAdminService.java
 * Description: 标签删除服务接口，定义标签删除相关的业务操作
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   提供标签删除相关的业务操作接口：
 *   1. 单个标签删除
 *   2. 批量标签删除
 * 
 * 接口功能：
 * 1. 支持单个标签的安全删除
 * 2. 支持多个标签的批量删除
 * 3. 所有操作都返回操作结果的布尔值
 */
package org.example.servicequestion.admin.tags.service;

import java.util.List;

public interface DeleteTagsAdminService {
    /**
     * 删除单个标签
     * @param id 标签ID
     * @return 是否删除成功
     */
    boolean deleteTag(Long id);

    /**
     * 批量删除标签
     * @param ids 标签ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteTags(List<Long> ids);
} 