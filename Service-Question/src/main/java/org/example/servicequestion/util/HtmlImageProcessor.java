/**
 * HtmlImageProcessor 是一个用于处理 HTML 内容中图片标签的工具类。
 * <p>
 * 它的主要功能是扫描 HTML 字符串中所有的 <img> 标签，查找其中 alt 属性以 # 开头的值，
 * 将该值（例如 "#image123"）视为图片标识符，从而通过远程 Feign 服务 {@link ServiceImageFeign}
 * 获取实际的图片访问 URL，并自动插入或替换为 <img> 标签中的 src 属性。
 * <p>
 * 该功能主要用于支持富文本编辑器输出中使用 alt="#xxx" 占位标记的场景，在页面展示前替换为真实图片地址。
 *
 * <p>例如输入：
 * <pre>{@code
 * <p>题目插图如下：</p>
 * <img alt="#math001">
 * }</pre>
 *
 * 处理后自动补全为：
 * <pre>{@code
 * <p>题目插图如下：</p>
 * <img src="https://cdn.example.com/image/math001.png" alt="#math001">
 * }</pre>
 *
 * <p><strong>注意事项：</strong>
 * <ul>
 *   <li>仅处理 alt 属性以 # 开头的 img 标签</li>
 *   <li>如果已存在 src 属性，则进行替换</li>
 *   <li>如果通过 Feign 服务未能获取到有效 URL，则跳过该标签</li>
 * </ul>
 *
 * @author holic512
 * @since 2025-03-07
 * @version 1.0
 * @see ServiceImageFeign
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


