/**
 * File Name: HtmlImageProcessor.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-03-07
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.util;

import org.example.servicequestion.feign.ServiceImageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HtmlImageProcessor {


    private final ServiceImageFeign serviceImageFeign;

    @Autowired
    public HtmlImageProcessor(ServiceImageFeign serviceImageFeign) {
        this.serviceImageFeign = serviceImageFeign;
    }


    // 正则表达式，匹配 <img> 标签，提取 alt 以 # 开头的
    private static final Pattern IMG_PATTERN = Pattern.compile(
            "<img\\s+([^>]*?)alt=[\"'](#[^\"']+)[\"'](.*?)>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


    public String insertSrcForImages(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }

        Matcher matcher = IMG_PATTERN.matcher(html);
        StringBuilder result = new StringBuilder();

        boolean found = false; // 记录是否匹配到 img
        while (matcher.find()) {
            found = true;
            String beforeAlt = matcher.group(1); // alt 之前的部分
            String altText = matcher.group(2);   // alt 内容（以 # 开头）
            String afterAlt = matcher.group(3);  // alt 之后的部分

            String imageName = altText.substring(1); // 去掉 "#"
            String srcValue = serviceImageFeign.getPreviewUrl(imageName);

            // // 调试输出
            // System.out.println("匹配到的 <img>：" + matcher.group(0));
            // System.out.println("提取的 altText：" + altText);
            // System.out.println("转换的 imageName：" + imageName);
            // System.out.println("获取的 srcValue：" + srcValue);

            if (srcValue == null || srcValue.isEmpty()) {
                // System.out.println("警告：未能获取到有效的 srcValue！");
                continue; // 跳过空值
            }

            // **确保不会重复插入 src**
            String replacement;
            if (beforeAlt.contains("src=")) {
                // 如果 <img> 原本就有 src，则替换它
                replacement = matcher.group(0).replaceAll("src=[\"'][^\"']*[\"']", "src=\"" + srcValue + "\"");
            } else {
                // **如果没有 src，则插入**
                replacement = "<img " + beforeAlt + "src=\"" + srcValue + "\" " + afterAlt + "alt=\"" + altText + "\">";
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        if (!found) {
            // System.out.println("没有匹配到任何 <img> 标签");
        }

        return result.toString();
    }
}


