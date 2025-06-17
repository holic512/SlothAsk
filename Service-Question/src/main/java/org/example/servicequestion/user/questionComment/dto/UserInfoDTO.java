/**
 * File Name: UserInfoDTO.java
 * Description: 用户信息DTO
 * Author: holic512
 * Created Date: 2025-04-20
 * Version: 1.0
 */
package org.example.servicequestion.user.questionComment.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    /**
     * 用户名称
     */
    private String nickname;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 用户名
     */
    private String username;
}