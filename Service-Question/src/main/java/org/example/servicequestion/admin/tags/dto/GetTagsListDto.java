/**
 * File Name: GetTagsListDto.java
 * Description: 标签列表数据传输对象，用于封装标签的完整信息
 * Author: holic512
 * Created Date: 2025-02-04
 * Version: 1.0
 * Usage: 
 *   用于在获取标签列表接口中传输标签数据，包含以下字段：
 *   - id: 标签唯一标识
 *   - projectId: 所属项目ID
 *   - name: 标签名称
 *   - description: 标签描述
 *   - sortOrder: 排序顺序
 *   - status: 标签状态
 *   - updateTime: 最后更新时间
 * 
 * 该DTO主要用于：
 * 1. 标签列表展示
 * 2. 标签详细信息查看
 * 3. 支持分页和搜索功能的数据传输
 */
package org.example.servicequestion.admin.tags.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetTagsListDto {
    private Long id;
    private Long projectId;
    private String name;
    private String description;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime updateTime;
}
