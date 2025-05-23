/**
 * File Name: SCryptUtil.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-14
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.util;

import org.bouncycastle.crypto.generators.SCrypt;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class SCryptUtil {

    private static final int SALT_LENGTH = 16; // 16 字节的盐长度，提供足够的随机性。
    private static final int KEY_LENGTH = 32; // 256 位的密钥长度，适合高安全需求。

    // 配置 SCrypt 参数
    private static final int N = 1024; // 2^10，计算复杂度较低，适应内存限制。
    private static final int r = 8;     // 每个块大小，保持在 8 字节，以节省内存。
    private static final int p = 1;     // 并行化参数，设置为 1

    /**
     * 生成 加密密码
     *
     * @param password 密码
     * @return 密码加密后的值
     */
    public static String hashPassword(String password) {
        // 生成随机盐
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        // 生成散列
        byte[] hash = SCrypt.generate(password.getBytes(), salt, N, r, p, KEY_LENGTH);

        // 以 Base64 格式存储盐和散列
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 验证密码是否匹配
     *
     * @param password   密码
     * @param storedHash 已加密的密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String password, String storedHash) {
        // 从存储的散列中分离盐和散列
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Stored hash is invalid.");
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHashBytes = Base64.getDecoder().decode(parts[1]);

        // 生成新的散列
        byte[] hash = SCrypt.generate(password.getBytes(), salt, N, r, p, KEY_LENGTH);

        // 比较散列
        return Arrays.equals(hash, storedHashBytes);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        System.out.println(hashPassword(password));

    }
}