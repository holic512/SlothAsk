/**
 * File Name: PageDto.java
 * Description: 分页数据传输对象
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage: 用于封装分页查询结果，包含总记录数和当前页的用户列表数据
 */
package org.example.serviceuser.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDto {
    private int total;
    private List<UserDto> list;

    public PageDto(int total, List<UserDto> list) {
        this.total = total;
        this.list = list;
    }
}
