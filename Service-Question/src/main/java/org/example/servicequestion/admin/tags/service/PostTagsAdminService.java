/**
 * File Name: PostTagsAdminService.java
 * Description: 标签添加服务接口，定义标签添加相关的业务操作
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   提供标签添加相关的业务操作接口：
 *   1. 新增标签
 * 
 * 接口功能：
 * 1. 处理标签添加的业务逻辑
 * 2. 数据验证和转换
 * 3. 返回添加操作的结果状态
 */
package org.example.servicequestion.admin.tags.service;

import org.example.servicequestion.admin.tags.enums.PostTagsAdminEnum;
import org.example.servicequestion.admin.tags.request.AddTagsRequest;

public interface PostTagsAdminService {

    PostTagsAdminEnum AddTag(AddTagsRequest addTagsRequest);
}
