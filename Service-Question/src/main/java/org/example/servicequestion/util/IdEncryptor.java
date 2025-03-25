/**
 * File Name: IdEncryptor.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-25
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class IdEncryptor {
    private static final String SECRET_KEY = "1234567890123456"; // 16字节密钥
    private static final String ALGORITHM = "AES";

    public static String encryptId(Long id) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM));
            byte[] encryptedBytes = cipher.doFinal(String.valueOf(id).getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    public static Long decryptId(String encryptedId) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM));

            // Base64 解码
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedId);

            // 执行解密
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            // 尝试将解密后的数据转换成 Long
            return Long.parseLong(new String(decryptedBytes));
        } catch (Exception e) {
            return null;
        }
    }


    public static void main(String[] args) {
        Long id = 123456L;
        String encryptedId = encryptId(id);

        System.out.println("原始 ID: " + id);
        System.out.println("加密 ID: " + encryptedId);

        long retrievedId = decryptId(encryptedId);
        System.out.println("解析出的 ID: " + retrievedId);
    }
}