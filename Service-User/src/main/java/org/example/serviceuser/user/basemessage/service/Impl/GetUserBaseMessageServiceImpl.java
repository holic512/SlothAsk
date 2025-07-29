/**
 * File Name: GetUserBaseMessageServiceImpl.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-07-29
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.user.basemessage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.servicecommon.entity.BaseMessage;
import org.example.serviceuser.user.basemessage.dto.BaseMessagePageQueryDto;
import org.example.serviceuser.user.basemessage.dto.BaseMessagePageResultDto;
import org.example.serviceuser.user.basemessage.dto.BaseMessageResponseDto;
import org.example.serviceuser.user.basemessage.mapper.UserBMBaseMessageMapper;
import org.example.serviceuser.user.basemessage.service.GetUserBaseMessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetUserBaseMessageServiceImpl implements GetUserBaseMessageService {
    
    private final UserBMBaseMessageMapper userBMBaseMessageMapper;
    
    @Override
    public boolean hasUnreadMessagesInLatestTen(Long userId) {
        if (userId == null) {
            return false;
        }
        
        // 查询用户最新创建的前10条消息
        QueryWrapper<BaseMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("created_time")
                   .last("LIMIT 10");
        
        List<BaseMessage> messages = userBMBaseMessageMapper.selectList(queryWrapper);
        
        // 检查是否存在未读消息（read_status = 0表示未读）
        return messages.stream().anyMatch(message -> message.getReadStatus() == 0);
    }
    
    @Override
    public List<BaseMessageResponseDto> getLatestTenMessages(Long userId) {
        if (userId == null) {
            return List.of();
        }
        
        // 查询用户最新创建的前10条消息
        QueryWrapper<BaseMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("created_time")
                   .last("LIMIT 10");
        
        List<BaseMessage> messages = userBMBaseMessageMapper.selectList(queryWrapper);
        
        // 转换为DTO，过滤掉updateTime字段
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public BaseMessagePageResultDto getMessagesByPage(Long userId, BaseMessagePageQueryDto queryDto) {
        if (userId == null || queryDto == null) {
            return new BaseMessagePageResultDto(1, 10, 0L, List.of());
        }
        
        // 创建分页对象
        Page<BaseMessage> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        
        // 构建查询条件
        QueryWrapper<BaseMessage> queryWrapper = buildQueryWrapper(userId, queryDto);
        
        // 执行分页查询
        IPage<BaseMessage> pageResult = userBMBaseMessageMapper.selectPage(page, queryWrapper);
        
        // 转换为DTO
        List<BaseMessageResponseDto> records = pageResult.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        // 返回分页结果
        return new BaseMessagePageResultDto(
                (int) pageResult.getCurrent(),
                (int) pageResult.getSize(),
                pageResult.getTotal(),
                records
        );
    }
    
    /**
     * 构建查询条件
     * @param userId 用户ID
     * @param queryDto 查询条件DTO
     * @return 查询包装器
     */
    private QueryWrapper<BaseMessage> buildQueryWrapper(Long userId, BaseMessagePageQueryDto queryDto) {
        QueryWrapper<BaseMessage> queryWrapper = new QueryWrapper<>();
        
        // 用户ID条件（必须）
        queryWrapper.eq("user_id", userId);
        
        // 消息类型过滤
        if (queryDto.getType() != null) {
            queryWrapper.eq("type", queryDto.getType());
        }
        
        // 阅读状态过滤
        if (queryDto.getReadStatus() != null) {
            queryWrapper.eq("read_status", queryDto.getReadStatus());
        }
        
        // 创建时间范围查询
        if (queryDto.getCreateTimeStart() != null) {
            queryWrapper.ge("created_time", queryDto.getCreateTimeStart());
        }
        if (queryDto.getCreateTimeEnd() != null) {
            queryWrapper.le("created_time", queryDto.getCreateTimeEnd());
        }
        
        // 关键词搜索（在messageData中搜索）
        if (StringUtils.hasText(queryDto.getKeyword())) {
            queryWrapper.like("message_data", queryDto.getKeyword());
        }
        
        // 排序
        String orderBy = StringUtils.hasText(queryDto.getOrderBy()) ? queryDto.getOrderBy() : "created_time";
        boolean isAsc = "asc".equalsIgnoreCase(queryDto.getOrderDirection());
        
        if (isAsc) {
            queryWrapper.orderByAsc(orderBy);
        } else {
            queryWrapper.orderByDesc(orderBy);
        }
        
        return queryWrapper;
    }
    
    @Override
    public int markLatestTenMessagesAsRead(Long userId) {
        if (userId == null) {
            return 0;
        }
        
        // 查询用户最新创建的前十条未读消息的ID
        QueryWrapper<BaseMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("read_status", 0) // 只查询未读消息
                   .orderByDesc("created_time")
                   .last("LIMIT 10");
        
        List<BaseMessage> unreadMessages = userBMBaseMessageMapper.selectList(queryWrapper);
        
        if (unreadMessages.isEmpty()) {
            return 0;
        }
        
        // 批量更新为已读状态
        List<Long> messageIds = unreadMessages.stream()
                .map(BaseMessage::getId)
                .collect(Collectors.toList());
        
        QueryWrapper<BaseMessage> updateWrapper = new QueryWrapper<>();
        updateWrapper.in("id", messageIds);
        
        BaseMessage updateEntity = new BaseMessage();
        updateEntity.setReadStatus(1); // 设置为已读
        
        return userBMBaseMessageMapper.update(updateEntity, updateWrapper);
    }
    
    @Override
    public int markAllMessagesAsRead(Long userId) {
        if (userId == null) {
            return 0;
        }
        
        // 构建更新条件：用户ID匹配且为未读状态
        QueryWrapper<BaseMessage> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("user_id", userId)
                    .eq("read_status", 0); // 只更新未读消息
        
        BaseMessage updateEntity = new BaseMessage();
        updateEntity.setReadStatus(1); // 设置为已读
        
        return userBMBaseMessageMapper.update(updateEntity, updateWrapper);
    }
    
    /**
     * 将BaseMessage转换为BaseMessageResponseDto
     * @param baseMessage 原始消息实体
     * @return 响应DTO（不包含updateTime字段）
     */
    private BaseMessageResponseDto convertToDto(BaseMessage baseMessage) {
        return new BaseMessageResponseDto(
                baseMessage.getId(),
                baseMessage.getType(),
                baseMessage.getReadStatus(),
                baseMessage.getMessageData(),
                baseMessage.getCreateTime()
        );
    }
}
