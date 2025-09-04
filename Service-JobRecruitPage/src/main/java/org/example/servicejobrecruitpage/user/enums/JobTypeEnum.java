/**
 * File Name: JobTypeEnum.java
 * Description: 岗位类型枚举，定义招聘岗位的类型选项
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于表示岗位类型的枚举值，与前端JobType枚举保持一致
 */
package org.example.servicejobrecruitpage.user.enums;

import lombok.Getter;

@Getter
public enum JobTypeEnum {
    /** 校招 - 校园招聘 */
    CAMPUS_RECRUITMENT("校招", "校园招聘"),
    /** 社招 - 社会招聘 */
    SOCIAL_RECRUITMENT("社招", "社会招聘"),
    /** 暑期实习 - 暑假期间的实习岗位 */
    SUMMER_INTERNSHIP("暑期实习", "暑假期间的实习岗位"),
    /** 寒假实习 - 寒假期间的实习岗位 */
    WINTER_INTERNSHIP("寒假实习", "寒假期间的实习岗位"),
    /** 春招 - 春季招聘 */
    SPRING_RECRUITMENT("春招", "春季招聘"),
    /** 秋招 - 秋季招聘 */
    AUTUMN_RECRUITMENT("秋招", "秋季招聘"),
    /** 日常实习 - 日常实习岗位 */
    DAILY_INTERNSHIP("日常实习", "日常实习岗位");

    private final String value;
    private final String description;

    JobTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 根据值获取枚举
     * @param value 枚举值
     * @return 对应的枚举，如果不存在则返回null
     */
    public static JobTypeEnum fromValue(String value) {
        for (JobTypeEnum type : JobTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}