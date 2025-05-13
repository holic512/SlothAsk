/**
 * File Name: UserAccountDTO.java
 * Description: 用户账户数据传输对象
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 用于在服务间传输用户账户信息（用户名和邮箱以及用户名剩余修改次数）
 */
package org.example.serviceuser.user.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 用户名剩余修改次数
     */
    private Integer remainingUsernameChanges;
    
    /**
     * 构造函数，仅包含用户名和邮箱
     * 
     * @param username 用户名
     * @param email 邮箱
     */
    public UserAccountDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }
}