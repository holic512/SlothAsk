/**
 * File Name: ResultDto.java
 * Description: Todo
 * Author: lv
 * Created Date: 2025-01-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.dto;

import lombok.Data;

@Data
public class ResultDto {
    private int status;
    private String message;
    private PageDto data;

    public ResultDto(int status, String message, PageDto data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
