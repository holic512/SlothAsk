/**
 * File Name: UserProfileDTO.java
 * Description: 用户个人资料数据传输对象
 * Author: holic512
 * Created Date: 2025-03-18
 * Version: 1.0
 * Usage:
 * 用于前后端数据交互，去除不需要的id和时间字段
 */
package org.example.serviceuser.user.info.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer age;
    private LocalDate birthday;
    private String location;
    private Integer occupation;
    private String bio;
    
    // 从实体类转换为DTO
    public static UserProfileDTO fromEntity(org.example.serviceuser.entity.UserProfile entity) {
        if (entity == null) {
            return null;
        }
        
        UserProfileDTO dto = new UserProfileDTO();
        dto.setNickname(entity.getNickname());
        dto.setAvatar(entity.getAvatar());
        dto.setGender(entity.getGender());
        dto.setAge(entity.getAge());
        dto.setBirthday(entity.getBirthday());
        dto.setLocation(entity.getLocation());
        dto.setOccupation(entity.getOccupation());
        dto.setBio(entity.getBio());
        
        return dto;
    }
    
    // 将DTO数据填充到实体类中
    public void toEntity(org.example.serviceuser.entity.UserProfile entity) {
        if (entity != null) {
            entity.setNickname(this.nickname);
            entity.setGender(this.gender);
            entity.setAge(this.age);
            entity.setBirthday(this.birthday);
            entity.setLocation(this.location);
            entity.setOccupation(this.occupation);
            entity.setBio(this.bio);
        }
    }
} 