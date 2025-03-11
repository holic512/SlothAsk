/**
 * 创建时间: 2024-09-02
 * 描述: UUID及数字ID生成工具类
 * 版本: 1.1
 * 作者: holic512
 */
package org.example.serviceuser.util;

import java.security.SecureRandom;
import java.util.UUID;

public class UuidUtil {
    private static final SecureRandom random = new SecureRandom();

    /**
     * 生成随机UUID
     *
     * @return UUID的字符串表示
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成8位数字ID
     *
     * @return 8位数字ID的字符串表示
     */
    public static String generate8DigitNumericId() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10)); // 生成0到9之间的随机数字
        }
        return sb.toString();
    }

    /**
     * 生成6位数字ID
     *
     * @return 6位数字ID的字符串表示
     */
    public static String generate6DigitNumericId() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // 生成0到9之间的随机数字
        }
        return sb.toString();
    }
}
