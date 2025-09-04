/**
 * File Name: UserJobApplication.java
 * Description: 用户岗位申请状态实体类
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 */
package org.example.servicecommon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_job_applications")
public class UserJobApplication {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("job_id")
    private Long jobId;

    @TableField("application_status")
    private String applicationStatus;

    @TableField("apply_time")
    private LocalDateTime applyTime;

    @TableField("status_update_time")
    private LocalDateTime statusUpdateTime;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}