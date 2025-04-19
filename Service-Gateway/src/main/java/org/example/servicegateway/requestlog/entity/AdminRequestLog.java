package org.example.servicegateway.requestlog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "admin_request_log")
public class AdminRequestLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 日志ID

    @TableField("admin_id")
    private Long adminId; // 管理员ID

    @TableField("request_time")
    private LocalDateTime requestTime; // 请求时间

    @TableField("request_path")
    private String requestPath; // 请求路径

}
