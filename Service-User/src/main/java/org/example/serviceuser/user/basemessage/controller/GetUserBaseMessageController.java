/**
 * File Name: GetUserBaseMessageController.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-07-29
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.basemessage.controller;

import lombok.RequiredArgsConstructor;
import org.example.servicecommon.ApiResponse.ApiResponse;
import org.example.serviceuser.user.basemessage.dto.BaseMessagePageQueryDto;
import org.example.serviceuser.user.basemessage.dto.BaseMessagePageResultDto;
import org.example.serviceuser.user.basemessage.dto.BaseMessageResponseDto;
import org.example.serviceuser.user.basemessage.service.GetUserBaseMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("user/basemessage")
public class GetUserBaseMessageController {
    
    private final GetUserBaseMessageService getUserBaseMessageService;
    
    /**
     * 检查用户最新创建的前十条消息中是否存在未读消息(返回true证明有未读信息)
     * @param userId 用户ID，从请求头X-User-Id获取
     * @return ApiResponse 包含检查结果的响应
     */
    @GetMapping("/check-unread-latest-ten")
    public ApiResponse checkUnreadMessagesInLatestTen(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return ApiResponse.error(400, "用户ID不能为空");
        }
        
        boolean hasUnreadMessages = getUserBaseMessageService.hasUnreadMessagesInLatestTen(userId);
         
         return ApiResponse.ok("检查完成", hasUnreadMessages);
     }
     
     /**
       * 获取用户最新创建的前十条消息（不包含updateTime字段）
       * @param userId 用户ID，从请求头X-User-Id获取
       * @return ApiResponse 包含前十条消息列表的响应
       */
      @GetMapping("/latest-ten")
      public ApiResponse getLatestTenMessages(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
          if (userId == null) {
              return ApiResponse.error(400, "用户ID不能为空");
          }
          
          List<BaseMessageResponseDto> messages = getUserBaseMessageService.getLatestTenMessages(userId);
          
          return ApiResponse.ok("获取成功", messages);
      }
      
      /**
       * 分页查询用户消息（支持多种过滤条件）
       * @param userId 用户ID，从请求头X-User-Id获取
       * @param queryDto 分页查询条件
       * @return ApiResponse 包含分页查询结果的响应
       */
      @PostMapping("/page")
      public ApiResponse getMessagesByPage(
              @RequestHeader(value = "X-User-Id", required = false) Long userId,
              @RequestBody BaseMessagePageQueryDto queryDto) {
          
          if (userId == null) {
              return ApiResponse.error(400, "用户ID不能为空");
          }
          
          // 设置默认分页参数
          if (queryDto.getPageNum() == null || queryDto.getPageNum() < 1) {
              queryDto.setPageNum(1);
          }
          if (queryDto.getPageSize() == null || queryDto.getPageSize() < 1 || queryDto.getPageSize() > 100) {
              queryDto.setPageSize(10);
          }
          
          BaseMessagePageResultDto result = getUserBaseMessageService.getMessagesByPage(userId, queryDto);
           return ApiResponse.ok("查询成功", result);
       }
       
       /**
        * 批量已读用户最新创建的前十条消息
        * @param userId 用户ID，从请求头X-User-Id获取
        * @return ApiResponse 包含更新数量的响应
        */
       @PutMapping("/mark-latest-ten-read")
       public ApiResponse markLatestTenMessagesAsRead(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
           if (userId == null) {
               return ApiResponse.error(400, "用户ID不能为空");
           }
           
           int updatedCount = getUserBaseMessageService.markLatestTenMessagesAsRead(userId);
           return ApiResponse.ok("批量已读成功，共更新 " + updatedCount + " 条消息", updatedCount);
       }
       
       /**
        * 批量已读用户的全部未读消息
        * @param userId 用户ID，从请求头X-User-Id获取
        * @return ApiResponse 包含更新数量的响应
        */
       @PutMapping("/mark-all-read")
       public ApiResponse markAllMessagesAsRead(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
           if (userId == null) {
               return ApiResponse.error(400, "用户ID不能为空");
           }
           
           int updatedCount = getUserBaseMessageService.markAllMessagesAsRead(userId);
           return ApiResponse.ok("批量已读成功，共更新 " + updatedCount + " 条消息", updatedCount);
       }
}
