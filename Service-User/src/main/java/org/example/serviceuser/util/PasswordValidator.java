/**
 * File Name: PasswordValidator.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-05-10
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceuser.util;

// PasswordValidator.java

import java.util.*;
import java.util.regex.Pattern;

public final class PasswordValidator {

    // 提供一个默认实例，方便快捷调用
    public static final PasswordValidator DEFAULT =
            new PasswordValidator(new Options.Builder().build());
    // 特殊字符正则表达式
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?\\\\|]");
    private final Options cfg;

    public PasswordValidator(Options options) {
        this.cfg = options;
    }

    /**
     * 核心校验方法：单次遍历字符串，收集所有信息
     */
    public ValidationResult validate(String pwd) {
        List<String> errors = new ArrayList<>(4);

        // 预处理输入，处理空字符串和null
        if (pwd == null) {
            errors.add("密码不能为空");
            return new ValidationResult(false, errors);
        }

        // 去除前后空格
        pwd = pwd.trim();
        int len = pwd.length();

        // 空密码检查
        if (len == 0) {
            errors.add("密码不能为空");
            return new ValidationResult(false, errors);
        }

        // 1. 长度校验
        if (len < cfg.minLength || len > cfg.maxLength) {
            errors.add(
                    String.format("长度必须在 %d 到 %d 个字符之间", cfg.minLength, cfg.maxLength)
            );
        }

        // 遍历标志
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        boolean hasSpecial = false, hasWhitespace = false;
        boolean repeatErrorReported = false;
        char last = '\0';
        int consecCount = 1;

        for (int i = 0; i < len; i++) {
            char c = pwd.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isWhitespace(c)) {
                hasWhitespace = true;
            } else if (SPECIAL_CHARS_PATTERN.matcher(String.valueOf(c)).matches()) {
                hasSpecial = true;
            }

            // 检查连续重复 - 修正逻辑
            if (i > 0 && c == last) {
                consecCount++;
            } else {
                consecCount = 1;
            }

            // 报告连续重复错误（如果未报告过）
            if (!repeatErrorReported && consecCount > cfg.maxConsecutiveRepeat) {
                errors.add(
                        String.format("不能有超过 %d 个连续相同字符", cfg.maxConsecutiveRepeat)
                );
                repeatErrorReported = true;
            }

            last = c;
        }

        // 2. 空白字符
        if (cfg.noWhitespace && hasWhitespace) {
            errors.add("不能包含空白字符");
        }
        // 3. 大小写、数字、特殊字符
        if (cfg.requireUppercase && !hasUpper) {
            errors.add("必须包含至少一个大写字母 (A-Z)");
        }
        if (cfg.requireLowercase && !hasLower) {
            errors.add("必须包含至少一个小写字母 (a-z)");
        }
        if (cfg.requireDigit && !hasDigit) {
            errors.add("必须包含至少一个数字 (0-9)");
        }
        if (cfg.requireSpecial && !hasSpecial) {
            errors.add("必须包含至少一个特殊字符（如 !@#$%^&*() 等）");
        }

        // 4. 弱密码/违禁密码检测
        String pwdLower = pwd.toLowerCase();

        // 根据配置选择全匹配或子串匹配
        if (cfg.useBannedSubstringMatch) {
            // 子串匹配 - 检查密码是否包含任何违禁词
            for (String banned : cfg.bannedSet) {
                if (pwdLower.contains(banned)) {
                    errors.add("密码包含常见弱密码或违禁词");
                    break;
                }
            }
        } else {
            // 全匹配 - 检查密码是否与任何违禁词完全相同
            if (cfg.bannedSet.contains(pwdLower)) {
                errors.add("禁止使用弱密码或违禁密码");
            }
        }

        boolean valid = errors.isEmpty();
        return new ValidationResult(valid, errors);
    }

    // 校验结果
    public record ValidationResult(boolean valid, List<String> errors) {
    }

    // 可配置项
    public static class Options {
        public final int minLength;
        public final int maxLength;
        public final boolean requireUppercase;
        public final boolean requireLowercase;
        public final boolean requireDigit;
        public final boolean requireSpecial;
        public final boolean noWhitespace;
        public final int maxConsecutiveRepeat;
        public final Set<String> bannedSet;
        public final boolean useBannedSubstringMatch;

        private Options(Builder b) {
            this.minLength = b.minLength;
            this.maxLength = b.maxLength;
            this.requireUppercase = b.requireUppercase;
            this.requireLowercase = b.requireLowercase;
            this.requireDigit = b.requireDigit;
            this.requireSpecial = b.requireSpecial;
            this.noWhitespace = b.noWhitespace;
            this.maxConsecutiveRepeat = b.maxConsecutiveRepeat;
            this.useBannedSubstringMatch = b.useBannedSubstringMatch;
            // 统一小写并构造 HashSet，便于快速查找
            Set<String> tmp = new HashSet<>();
            for (String p : b.bannedList) {
                if (p != null && !p.isEmpty()) {
                    tmp.add(p.toLowerCase());
                }
            }
            this.bannedSet = Collections.unmodifiableSet(tmp);
        }

        public static class Builder {
            private int minLength = 8;
            private int maxLength = 32;
            private boolean requireUppercase = true;
            private boolean requireLowercase = true;
            private boolean requireDigit = true;
            private boolean requireSpecial = true;
            private boolean noWhitespace = true;
            private int maxConsecutiveRepeat = 2;
            private boolean useBannedSubstringMatch = true;
            private List<String> bannedList = List.of(
                    "password", "123456", "12345678", "123456789", "1234567890",
                    "qwerty", "qwerty123", "abc123", "abc12345", "111111", "11111111",
                    "123123", "121212", "password1", "passw0rd", "letmein", "welcome",
                    "dragon", "sunshine", "master", "football", "baseball", "monkey",
                    "shadow", "superman", "michael", "mustang", "password123",
                    "pokemon", "iloveyou", "696969", "1234", "12345", "1234abcd",
                    "000000", "qazwsx", "123qwe", "123abc", "1q2w3e4r", "zaq1zaq1",
                    "admin", "administrator", "root", "sysadmin", "support", "staff",
                    "pass123", "login", "hello", "freedom", "whatever", "trustno1",
                    "flower", "hottie", "lovely", "zaq12wsx", "baseball1"
            );

            public Builder minLength(int len) {
                this.minLength = len;
                return this;
            }

            public Builder maxLength(int len) {
                this.maxLength = len;
                return this;
            }

            public Builder requireUppercase(boolean f) {
                this.requireUppercase = f;
                return this;
            }

            public Builder requireLowercase(boolean f) {
                this.requireLowercase = f;
                return this;
            }

            public Builder requireDigit(boolean f) {
                this.requireDigit = f;
                return this;
            }

            public Builder requireSpecial(boolean f) {
                this.requireSpecial = f;
                return this;
            }

            public Builder noWhitespace(boolean f) {
                this.noWhitespace = f;
                return this;
            }

            public Builder maxConsecutiveRepeat(int n) {
                this.maxConsecutiveRepeat = n;
                return this;
            }

            public Builder bannedList(List<String> list) {
                this.bannedList = list;
                return this;
            }

            public Builder useBannedSubstringMatch(boolean f) {
                this.useBannedSubstringMatch = f;
                return this;
            }

            public Options build() {
                return new Options(this);
            }
        }
    }
}
