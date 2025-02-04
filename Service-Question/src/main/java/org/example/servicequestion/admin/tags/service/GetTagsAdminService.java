/**
 * File Name: GetTagsAdminService.java
 * Description: 标签查询服务接口，定义标签查询相关的业务操作
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   提供标签查询相关的业务操作接口：
 *   1. 获取项目列表
 *   2. 分页查询标签列表
 * 
 * 接口功能：
 * 1. 查询所有可用的项目列表
 * 2. 支持标签的分页查询
 * 3. 支持按关键字搜索标签
 * 4. 返回统一的分页数据格式
 */
package org.example.servicequestion.admin.tags.service;

import java.util.List;
import java.util.Map;

import org.example.servicequestion.admin.tags.dto.GetProjectListDto;

public interface GetTagsAdminService {

    List<GetProjectListDto> getProjectList();

    /**
     * 分页查询标签列表
     * @param keyword 搜索关键字
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Map<String, Object> getTagsList(String keyword, int pageNum, int pageSize);
}
