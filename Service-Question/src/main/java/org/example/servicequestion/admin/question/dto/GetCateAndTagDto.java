/**
 * File Name: GetCateAndTagDto.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-22
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetCateAndTagDto {
    List<GetCateDto> cateDtoList;
    List<GetTagDto> tagDtoList;

    public GetCateAndTagDto(List<GetCateDto> result1, List<GetTagDto> result2) {
        this.cateDtoList = result1;
        this.tagDtoList = result2;
    }
}
