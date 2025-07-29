/**
 * File Name: PostUserBaseMessageServiceImpl.java
 * Description: 用户基础消息POST操作服务实现类
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * 实现用户基础消息POST操作的具体业务逻辑
 */
package org.example.serviceuser.user.basemessage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.example.servicecommon.entity.BaseMessage;
import org.example.serviceuser.user.basemessage.mapper.UserBMBaseMessageMapper;
import org.example.serviceuser.user.basemessage.service.PostUserBaseMessageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostUserBaseMessageServiceImpl implements PostUserBaseMessageService {
    
    private final UserBMBaseMessageMapper userBMBaseMessageMapper;
    
    @Override
    public boolean markMessageAsRead(Long userId, Long messageId) {
        if (userId == null || messageId == null) {
            return false;
        }
        
        // 首先检查消息是否存在且属于该用户
        QueryWrapper<BaseMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", messageId)
                   .eq("user_id", userId);
        
        BaseMessage message = userBMBaseMessageMapper.selectOne(queryWrapper);
        
        if (message == null) {
            return false; // 消息不存在或不属于该用户
        }
        
        // 如果消息已经是已读状态，直接返回成功
        if (message.getReadStatus() == 1) {
            return true;
        }
        
        // 更新消息为已读状态
        UpdateWrapper<BaseMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", messageId)
                    .eq("user_id", userId)
                    .set("read_status", 1)
                    .set("update_time", "NOW()");
        
        int updateCount = userBMBaseMessageMapper.update(null, updateWrapper);
        
        return updateCount > 0;
    }
}