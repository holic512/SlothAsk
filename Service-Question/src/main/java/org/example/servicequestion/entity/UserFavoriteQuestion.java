/**
 * File Name: UserFavoriteQuestion.java
 * Description: 实体类，用于映射用户收藏题目的数据库记录，包含用户ID、题目ID及收藏时间等字段。
 * Author: holic512
 * Created Date: 2025-04-28
 * Version: 1.0
 * Usage:
 * 可作为 MyBatis 或 JPA 等 ORM 框架的数据模型使用，也可用于服务间传输收藏记录数据。
 */

package org.example.servicequestion.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户题目收藏表实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteQuestion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 收藏记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 收藏时间
     */
    private LocalDateTime createTime;
}

