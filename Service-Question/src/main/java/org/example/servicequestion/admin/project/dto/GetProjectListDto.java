package org.example.servicequestion.admin.project.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GetProjectListDto {
    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 