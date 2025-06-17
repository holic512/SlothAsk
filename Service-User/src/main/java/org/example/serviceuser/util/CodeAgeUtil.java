/**
 * File Name: CodeAgeUtil.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-13
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class CodeAgeUtil {

    /**
     * 计算码龄（字符串形式）
     *
     * @param startDate 开始时间（如注册时间）
     * @return 码龄字符串，例如 "0年"、"1年"、"2年"
     */
    public static String calculateCodeAgeStr(Date startDate) {
        if (startDate == null) {
            return "0年";
        }

        LocalDate start = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate now = LocalDate.now();

        int years = Math.max(Period.between(start, now).getYears(), 0);
        return years + "年";
    }

    // 如果项目里直接用 LocalDate，可重载一个版本
    public static String calculateCodeAgeStr(LocalDate start) {
        if (start == null) {
            return "0年";
        }
        int years = Math.max(Period.between(start, LocalDate.now()).getYears(), 0);
        return years + "年";
    }

    /**
     * 计算码龄（LocalDateTime 版，新增）
     */
    public static String calculateCodeAgeStr(LocalDateTime startDateTime) {
        if (startDateTime == null) {
            return "0年";
        }
        // 仅比较日期部分，忽略具体时分秒
        LocalDate start = startDateTime.toLocalDate();
        return calculateCodeAgeStr(start);
    }
}
