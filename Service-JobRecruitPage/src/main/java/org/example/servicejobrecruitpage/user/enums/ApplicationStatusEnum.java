/**
 * File Name: ApplicationStatusEnum.java
 * Description: 申请状态枚举，定义求职申请的进度状态
 * Author: holic512
 * Created Date: 2024-12-19
 * Version: 1.0
 * Usage:
 * 用于表示申请状态的枚举值，与前端ApplicationStatus枚举保持一致
 */
package org.example.servicejobrecruitpage.user.enums;

import lombok.Getter;

@Getter
public enum ApplicationStatusEnum {
    /** 待投递 - 尚未投递简历 */
    PENDING_SUBMISSION("待投递", "尚未投递简历"),
    /** 投递中 - 已投递简历，等待回复 */
    SUBMITTED("投递中", "已投递简历，等待回复"),
    /** 待笔试 - 通过初筛，等待笔试 */
    PENDING_WRITTEN_TEST("待笔试", "通过初筛，等待笔试"),
    /** 笔试中 - 正在进行笔试 */
    WRITTEN_TEST_IN_PROGRESS("笔试中", "正在进行笔试"),
    /** 一面 - 第一轮面试 */
    FIRST_INTERVIEW("一面", "第一轮面试"),
    /** 二面 - 第二轮面试 */
    SECOND_INTERVIEW("二面", "第二轮面试"),
    /** 三面 - 第三轮面试 */
    THIRD_INTERVIEW("三面", "第三轮面试");

    private final String value;
    private final String description;

    ApplicationStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 根据值获取枚举
     * @param value 枚举值
     * @return 对应的枚举，如果不存在则返回null
     */
    public static ApplicationStatusEnum fromValue(String value) {
        for (ApplicationStatusEnum status : ApplicationStatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}