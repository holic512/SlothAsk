/**
 * File Name: GetQuestionListRequest.java
 * Description: 获取题目列表的请求对象
 * Author: holic512
 * Created Date: 2025-03-21
 * Version: 1.0
 * Usage:
 * 用于接收前端获取题目列表时的过滤条件参数
 */
package org.example.servicequestion.user.study.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 获取题目列表的请求对象
 * 包含各种过滤条件参数
 */
@Data
public class GetQuestionListRequest {
    /** 搜索关键词 */
    private String searchText;
    
    /** 分类过滤 */
    private Integer filterCategory;
    
    /** 标签过滤列表 */
    private List<Integer> filterTags;
    
    /** 类型过滤 */
    private Integer filterType;
    
    /** 难度过滤 */
    private Integer filterDifficulty;

    /** 页码(必填) */
    @NotNull(message = "页码不能为空")
    private Integer pageNum;
}
