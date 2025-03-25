/**
 * File Name: CategoryRecommendDTO.java
 * Description: 题库分类推荐的数据传输对象
 * Author: holic512
 * Created Date: 2024-03-21
 * Version: 1.0
 */
package org.example.servicequestion.user.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRecommendDTO {
    private Long id;
    private String name;
    private String description;
    private String avatarUrl;
    private Long viewCount;
} 