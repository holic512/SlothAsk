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
    
    /** 条件匹配模式：true=全部满足(AND)，false=任一满足(OR)，默认为true */
    private Boolean matchAllConditions = true;
    
    /** 分类过滤 - 等于 */
    private Integer filterCategoryEquals;
    
    /** 分类过滤 - 不等于 */
    private Integer filterCategoryNotEquals;
    
    /** 标签过滤列表 */
    private List<Integer> filterTags;
    
    /** 类型过滤 - 等于 */
    private Integer filterTypeEquals;
    
    /** 类型过滤 - 不等于 */
    private Integer filterTypeNotEquals;
    
    /** 难度过滤 - 等于 */
    private Integer filterDifficultyEquals;
    
    /** 难度过滤 - 不等于 */
    private Integer filterDifficultyNotEquals;

    /** 页码(必填) */
    @NotNull(message = "页码不能为空")
    private Integer pageNum;
    
    // 为了向后兼容，保留原有字段的getter方法
    public Integer getFilterCategory() {
        return filterCategoryEquals;
    }
    
    public Integer getFilterType() {
        return filterTypeEquals;
    }
    
    public Integer getFilterDifficulty() {
        return filterDifficultyEquals;
    }
}
