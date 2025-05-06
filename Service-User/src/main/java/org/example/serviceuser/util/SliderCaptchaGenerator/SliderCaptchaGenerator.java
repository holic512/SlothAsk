package org.example.serviceuser.util.SliderCaptchaGenerator;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Random;

import static org.example.serviceuser.util.SliderCaptchaGenerator.RandomImageLoader.GetRandomImageLoader;

/**
 * 滑块验证码生成器
 * 用于生成滑块验证码的背景图和滑块图片，支持图片压缩和Base64编码
 * <p>
 * 主要功能：
 * 1. 生成带有滑块缺口的背景图
 * 2. 生成可拖动的滑块图片
 * 3. 添加干扰块防止AI自动识别
 * 4. 支持图片压缩和Base64编码输出
 *
 * @author holic512
 * @version 1.0
 * @since 2025-01-14
 */
@Service
public class SliderCaptchaGenerator {

    /**
     * 滑块的宽度
     */
    private static final int SLIDER_WIDTH = 50;

    /**
     * 滑块的高度
     */
    private static final int SLIDER_HEIGHT = 50;

    /**
     * 添加的误导块数量，用于防止AI自动识别
     */
    private static final int DECOY_BLOCKS = 3;

    /**
     * 图片压缩质量，范围0.0-1.0
     */
    private static final float IMAGE_QUALITY = 0.75f;

    /**
     * 生成滑块验证码
     * 包括背景图、滑块图片及其位置信息
     * <p>
     * 生成步骤：
     * 1. 获取随机背景图片
     * 2. 调整图片大小以提高性能
     * 3. 生成随机滑块位置
     * 4. 创建滑块图片
     * 5. 在背景图上创建滑块缺口
     * 6. 添加干扰块
     * 7. 压缩并转换为Base64编码
     *
     * @return 滑块验证码结果对象，包含背景图和滑块的Base64编码及位置信息
     * @throws IOException 当图片处理过程中发生IO异常时抛出
     */
    public static SliderCaptchaResult generateCaptcha() throws IOException {
        // 获取图片
        InputStream inputStream = GetRandomImageLoader();
        BufferedImage originalImage = ImageIO.read(inputStream);

        // 调整图片大小以提高性能
        int targetWidth = 320; // 目标宽度
        float ratio = (float) targetWidth / originalImage.getWidth();
        int targetHeight = Math.round(originalImage.getHeight() * ratio);

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        // 使用调整大小后的图片
        originalImage = resizedImage;
        int imageWidth = originalImage.getWidth();
        int imageHeight = originalImage.getHeight();

        Random random = new Random();
        int x = random.nextInt(imageWidth - SLIDER_WIDTH - 100) + 50; // 确保滑块不会太靠边
        int y = random.nextInt(imageHeight - SLIDER_HEIGHT - 100) + 50;

        // 创建滑块形状 - 使用圆角矩形
        BufferedImage sliderImage = new BufferedImage(SLIDER_WIDTH, SLIDER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sliderG2d = sliderImage.createGraphics();

        // 抗锯齿设置
        sliderG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        sliderG2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 创建圆角矩形形状
        RoundRectangle2D sliderShape = new RoundRectangle2D.Float(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT, 10, 10);

        // 设置裁剪区域为圆角矩形
        sliderG2d.setClip(sliderShape);

        // 从原图剪切滑块
        sliderG2d.drawImage(originalImage.getSubimage(x, y, SLIDER_WIDTH, SLIDER_HEIGHT), 0, 0, null);

        // 重置裁剪区域
        sliderG2d.setClip(null);

        // 添加滑块边缘效果
        sliderG2d.setComposite(AlphaComposite.SrcAtop);
        sliderG2d.setColor(new Color(255, 255, 255, 60));
        sliderG2d.draw(sliderShape);

        // 添加滑块阴影效果
        sliderG2d.setColor(new Color(0, 0, 0, 80));
        sliderG2d.setStroke(new BasicStroke(2f));
        sliderG2d.draw(sliderShape);

        sliderG2d.dispose();

        // 创建带有滑块缺口的背景图
        BufferedImage backgroundImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D bgG2d = backgroundImage.createGraphics();
        bgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bgG2d.drawImage(originalImage, 0, 0, null);

        // 创建滑块缺口 - 使用深色阴影而非绿色
        RoundRectangle2D shape = new RoundRectangle2D.Float(x, y, SLIDER_WIDTH, SLIDER_HEIGHT, 10, 10);

        // 先绘制深色阴影
        bgG2d.setColor(new Color(0, 0, 0, 120)); // 深色阴影
        bgG2d.fill(shape);

        // 添加误导块 - 用于防止AI检测 (在挖空前添加)
        for (int i = 0; i < DECOY_BLOCKS; i++) {
            int decoyX = random.nextInt(imageWidth - SLIDER_WIDTH - 20) + 10;
            int decoyY = random.nextInt(imageHeight - SLIDER_HEIGHT - 20) + 10;

            // 确保误导块与真实滑块不重叠
            if (Math.abs(decoyX - x) < SLIDER_WIDTH && Math.abs(decoyY - y) < SLIDER_HEIGHT) {
                continue;
            }

            // 绘制深色阴影的误导块
            bgG2d.setColor(new Color(0, 0, 0, 40 + random.nextInt(30)));
            RoundRectangle2D decoyShape = new RoundRectangle2D.Float(decoyX, decoyY,
                    SLIDER_WIDTH - 10 + random.nextInt(20),
                    SLIDER_HEIGHT - 10 + random.nextInt(20),
                    8, 8);
            bgG2d.fill(decoyShape);
        }

        // 然后清除真实滑块区域
        bgG2d.setComposite(AlphaComposite.DstOut);
        bgG2d.fill(shape);

        bgG2d.dispose();

        // 将图片转换为Base64字符串，并进行压缩
        String sliderBase64 = compressAndConvertToBase64PNG(sliderImage); // 滑块图像使用PNG保留透明度
        String backgroundBase64 = compressAndConvertToBase64(backgroundImage);

        return new SliderCaptchaResult(sliderBase64, backgroundBase64, x, y);
    }

    /**
     * 压缩图片并转换为Base64编码字符串 (JPEG格式)
     * 适用于背景图片的压缩和编码
     * <p>
     * 处理步骤：
     * 1. 处理带Alpha通道的图像，转换为RGB格式
     * 2. 使用JPEG格式进行压缩
     * 3. 转换为Base64字符串
     *
     * @param image 要压缩和编码的图片
     * @return Base64编码的字符串
     * @throws IOException 当图片处理过程中发生IO异常时抛出
     */
    private static String compressAndConvertToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 处理带有Alpha通道的图像
        if (image.getType() == BufferedImage.TYPE_INT_ARGB || image.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
            // 创建一个不带Alpha通道的新图像
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            // 创建白色背景
            Graphics2D g = newImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            // 在白色背景上绘制原图
            g.drawImage(image, 0, 0, null);
            g.dispose();

            // 使用转换后的图像
            image = newImage;
        }

        // 使用JPG格式进行压缩
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(IMAGE_QUALITY);

        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(imageOutputStream);
        writer.write(null, new javax.imageio.IIOImage(image, null, null), writeParam);

        writer.dispose();
        imageOutputStream.close();

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 将带透明度的图像转换为PNG格式的Base64字符串
     * 适用于滑块图片的编码，保留透明度信息
     * <p>
     * 特点：
     * - 使用PNG格式保持图片质量
     * - 保留透明度信息
     * - 不进行压缩处理
     *
     * @param image 要编码的图片
     * @return Base64编码的字符串
     * @throws IOException 当图片处理过程中发生IO异常时抛出
     */
    private static String compressAndConvertToBase64PNG(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 使用PNG格式保留透明度
        ImageIO.write(image, "png", outputStream);

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 滑块验证码结果类
     * 包含生成的滑块验证码的所有必要信息
     * <p>
     * 包含信息：
     * - 滑块图片的Base64编码
     * - 背景图片的Base64编码
     * - 滑块的X轴位置
     * - 滑块的Y轴位置
     */
    public static class SliderCaptchaResult {
        /**
         * 滑块图片的Base64编码
         */
        private final String sliderImageBase64;

        /**
         * 背景图片的Base64编码
         */
        private final String backgroundImageBase64;

        /**
         * 滑块的X轴位置
         */
        private final int xPosition;

        /**
         * 滑块的Y轴位置
         */
        private final int yPosition;

        /**
         * 构造函数
         * 初始化滑块验证码结果对象
         *
         * @param sliderImageBase64     滑块图片的Base64编码
         * @param backgroundImageBase64 背景图片的Base64编码
         * @param xPosition             滑块的X轴位置
         * @param yPosition             滑块的Y轴位置
         */
        public SliderCaptchaResult(String sliderImageBase64, String backgroundImageBase64, int xPosition, int yPosition) {
            this.sliderImageBase64 = sliderImageBase64;
            this.backgroundImageBase64 = backgroundImageBase64;
            this.xPosition = xPosition;
            this.yPosition = yPosition;
        }

        // Getter方法

        /**
         * 获取滑块图片的Base64编码
         *
         * @return 滑块图片的Base64编码字符串
         */
        public String getSliderImageBase64() {
            return sliderImageBase64;
        }

        /**
         * 获取背景图片的Base64编码
         *
         * @return 背景图片的Base64编码字符串
         */
        public String getBackgroundImageBase64() {
            return backgroundImageBase64;
        }

        /**
         * 获取滑块的X轴位置
         *
         * @return 滑块的X轴位置值
         */
        public int getXPosition() {
            return xPosition;
        }

        /**
         * 获取滑块的Y轴位置
         *
         * @return 滑块的Y轴位置值
         */
        public int getYPosition() {
            return yPosition;
        }
    }
}
