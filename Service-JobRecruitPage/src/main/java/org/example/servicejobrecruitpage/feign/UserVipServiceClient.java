/**
 * File Name: UserVipServiceClient.java
 * Description: 用户VIP服务Feign客户端
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 通过Feign调用Service-Vip服务的接口
 */
package org.example.servicejobrecruitpage.feign;

import org.example.servicecommon.ApiResponse.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "service-vip", path = "/vip")
public interface UserVipServiceClient {
    
    /**
     * 检查用户VIP是否生效
     * @param userId 用户ID
     * @return VIP是否生效
     */
    @GetMapping("/check")
    ApiResponse checkVipStatus(@RequestHeader("X-User-Id") Long userId);
    
    /**
     * 检查用户VIP是否生效（专供Feign调用）
     * @param userId 用户ID
     * @return VIP是否生效（直接返回Boolean）
     */
    @GetMapping("/check-feign")
    Boolean checkVipStatusForFeign(@RequestHeader("X-User-Id") Long userId);
}