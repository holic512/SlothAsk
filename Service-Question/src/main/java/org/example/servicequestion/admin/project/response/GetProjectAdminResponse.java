package org.example.servicequestion.admin.project.response;

import lombok.Data;
import org.example.servicequestion.admin.project.dto.GetProjectListDto;
import java.util.List;

@Data
public class GetProjectAdminResponse {
    // 总记录数
    private Long total;
    // 项目列表
    private List<GetProjectListDto> list;
} 