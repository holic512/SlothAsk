/**
 * SCrypt密码加密工具类
 * 使用SCrypt算法进行密码的加密和验证，提供高安全性的密码存储方案
 *
 * 特点：
 * 1. 使用SCrypt算法，提供较高的安全性
 * 2. 自动生成随机盐值
 * 3. 支持密码验证
 * 4. 使用Base64编码存储
 *
 * @author holic512
 * @version 1.0
 * @since 2025-01-14
 */
package org.example.serviceuser.util;

import org.bouncycastle.crypto.generators.SCrypt;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class SCryptUtil {

    /**
     * 盐值长度（字节）
     * 16字节的盐长度提供足够的随机性，防止彩虹表攻击
     */
    private static final int SALT_LENGTH = 16;

    /**
     * 密钥长度（字节）
     * 256位的密钥长度，适合高安全需求
     */
    private static final int KEY_LENGTH = 32;

    /**
     * SCrypt算法参数
     * N: CPU/内存开销参数，必须是2的幂
     * r: 块大小参数，影响内存和CPU开销
     * p: 并行化参数，影响CPU开销
     */
    private static final int N = 1024; // 2^10，计算复杂度适中
    private static final int r = 8;    // 每个块大小8字节，平衡性能和内存使用
    private static final int p = 1;    // 不使用并行化，适合单线程环境

    /**
     * 使用SCrypt算法对密码进行加密
     * 
     * 加密步骤：
     * 1. 生成随机盐值
     * 2. 使用SCrypt算法生成密码散列
     * 3. 将盐值和散列组合并进行Base64编码
     *
     * @param password 待加密的原始密码
     * @return 格式为"base64(salt):base64(hash)"的加密字符串
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
     * 验证密码是否与存储的散列匹配
     * 
     * 验证步骤：
     * 1. 从存储的散列中提取盐值和散列值
     * 2. 使用相同的盐值和参数重新计算密码散列
     * 3. 比较计算得到的散列与存储的散列
     *
     * @param password 待验证的密码
     * @param storedHash 存储的散列值（格式：base64(salt):base64(hash)）
     * @return 如果密码匹配返回true，否则返回false
     * @throws IllegalArgumentException 当存储的散列格式无效时抛出
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

    /**
     * 主方法，用于测试密码加密功能
     * 从控制台读取密码并输出其加密后的散列值
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        System.out.println(hashPassword(password));
    }
}