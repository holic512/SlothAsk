/**
 * File Name: JobItem.java
 * Description: 岗位招聘信息实体类
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "job_items", autoResultMap = true)
public class JobItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("company_name")
    private String companyName;

    @TableField("job_name")
    private String jobName;

    @TableField("job_description")
    private String jobDescription;

    @TableField("job_type")
    private String jobType;

    // JSON 字段需要类型处理器
    @TableField(value = "benefits", typeHandler = JacksonTypeHandler.class)
    private List<String> benefits;

    @TableField("location")
    private String location;

    @TableField("salary_range")
    private String salaryRange;

    @TableField("publish_time")
    private LocalDateTime publishTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("apply_url")
    private String applyUrl;

    @TableField("referral_code")
    private String referralCode;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}