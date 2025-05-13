/**
 * File Name: GetUserAccountServiceImpl.java
 * Description: 获取用户账户信息的服务实现
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 提供获取用户账户信息的方法实现
 */
package org.example.serviceuser.user.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.serviceuser.entity.User;
import org.example.serviceuser.entity.UserAuth;
import org.example.serviceuser.user.account.dto.UserAccountDTO;
import org.example.serviceuser.user.account.mapper.UserAccountMapper;
import org.example.serviceuser.user.account.service.GetUserAccountService;
import org.example.serviceuser.user.account.service.PutUserAccountService;
import org.example.serviceuser.user.sign.mapper.UserAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserAccountServiceImpl implements GetUserAccountService {

    @Autowired
    private UserAccountMapper userMapper;
    
    @Autowired
    private PutUserAccountService putUserAccountService;
    
    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public UserAccountDTO getUsernameAndEmail(Long userId) {
        // 参数校验
        if (userId == null) {
            return null;
        }

        // 使用MyBatis-Plus的查询构造器，只查询username和email字段
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username", "email").eq("id", userId);
        
        // 获取用户信息，只查询指定字段
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }

        // 获取用户名剩余修改次数
        int remainingChanges = putUserAccountService.getRemainingUsernameChanges(userId);
        
        // 返回用户名、邮箱和剩余修改次数信息
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setUsername(user.getUsername());
        userAccountDTO.setEmail(user.getEmail());
        userAccountDTO.setRemainingUsernameChanges(remainingChanges);
        
        return userAccountDTO;
    }
    
    @Override
    public Boolean isPasswordEmpty(Long userId) {
        // 参数校验
        if (userId == null) {
            return null;
        }
        
        // 使用MyBatis-Plus的查询构造器，只查询password字段
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("password").eq("id", userId);
        
        // 获取用户信息，只查询密码字段
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        
        // 检查密码是否为null或空字符串
        String password = user.getPassword();
        return password == null || password.isEmpty();
    }
    
    @Override
    public Boolean isGithubBound(Long userId) {
        // 参数校验
        if (userId == null) {
            return null;
        }
        
        // 首先检查用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        
        // 查询用户是否绑定了GitHub账号
        UserAuth userAuth = userAuthMapper.findByUserIdAndProvider(userId, "github");
        
        // 如果userAuth不为空，则表示已绑定
        return userAuth != null;
    }
}