/**
 * File Name: PageDto.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
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
