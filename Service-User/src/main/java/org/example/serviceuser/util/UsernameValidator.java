/**
 * File Name: UsernameValidator.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-05-09
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.util;

// UsernameValidator.java

import java.util.*;
import java.util.regex.Pattern;

public class UsernameValidator {

    // 默认违禁词列表（可根据需要扩展）
    private static final Set<String> DEFAULT_BANNED_WORDS = new HashSet<>(Arrays.asList(
            "admin", "administrator", "root", "sysadmin", "superuser",
            "manager", "moderator", "operator", "support", "helpdesk",
            "staff", "owner", "system", "guest", "test", "info",
            "host", "developer", "rootuser", "supervisor"
    ));
    // 预编译正则模式
    private static final Pattern VALID_CHAR_PATTERN = Pattern.compile("^[A-Za-z0-9_.-]+$");
    private static final Pattern INVALID_EDGE_PATTERN = Pattern.compile("^[.-]|[.-]$");
    private static final Pattern DOUBLE_DOT_OR_DASH_PATTERN = Pattern.compile("(\\.|-){2,}");

    /**
     * 校验用户名
     *
     * @param username 用户名
     * @return 校验结果
     */
    public static ValidationResult validateUsername(String username) {
        return validateUsername(username, DEFAULT_BANNED_WORDS);
    }

    /**
     * 校验用户名（可指定违禁词）
     *
     * @param username    用户名
     * @param bannedWords 违禁词集合（不区分大小写）
     * @return 校验结果
     */
    public static ValidationResult validateUsername(String username, Set<String> bannedWords) {
        List<String> errors = new ArrayList<>();

        // 1. 长度校验
        int length = username.length();
        if (length < 4 || length > 20) {
            errors.add("长度必须在 4 到 20 个字符之间");
        }

        // 2. 字符合法性校验
        if (!VALID_CHAR_PATTERN.matcher(username).matches()) {
            errors.add("仅允许英文字母、数字、下划线（_）、点（.）或连字符（-）");
        }

        // 3. 首尾不能是点或连字符
        if (INVALID_EDGE_PATTERN.matcher(username).find()) {
            errors.add("点（.）或连字符（-）不能作为首尾字符");
        }

        // 4. 连续点或连字符
        if (DOUBLE_DOT_OR_DASH_PATTERN.matcher(username).find()) {
            errors.add("点（.）或连字符（-）不能连续出现（如 \"..\"、\"--\"）");
        }

        // 5. 违禁词检查（不区分大小写）
        String lower = username.toLowerCase();
        for (String word : bannedWords) {
            if (!word.isEmpty() && lower.contains(word.toLowerCase())) {
                errors.add("用户名不能包含违禁词 \"" + word + "\"");
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    // 校验结果类
    public record ValidationResult(boolean valid, List<String> errors) {
    }
}
