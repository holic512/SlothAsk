package org.example.serviceuser.util.SliderCaptchaGenerator;

import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 随机图片加载器
 * 用于从预定义的图片资源中随机加载一张图片，作为滑块验证码的背景图
 *
 * @author holic512
 * @version 1.0
 * @since 2025-01-14
 */
public class RandomImageLoader {
    
    /**
     * 获取随机验证码背景图片
     * 从public/captchaImage/目录下随机选择一张图片（1-15.jpg）
     *
     * @return 随机选择的图片文件
     * @throws IOException 当图片文件不存在或无法访问时抛出异常
     */
    public static File GetRandomImageLoader() throws IOException {
        // 1. 生成 1-15 之间的随机数
        Random random = new Random();
        int imageNumber = random.nextInt(15) + 1;  // 生成 1 到 15 之间的数字

        // 2. 构造文件名
        String fileName = "public/captchaImage/" + imageNumber + ".jpg";

        // 3. 获取文件路径
        ClassPathResource resource = new ClassPathResource(fileName);

        // 4. 确保文件存在
        File file = resource.getFile();
        if (!file.exists()) {
            throw new IOException("Captcha image not found: " + file.getAbsolutePath());
        }

        return file;
    }
}
