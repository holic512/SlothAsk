/**
 * File Name: HtmlTextExtractor.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public final class HtmlTextExtractor {

    // 可复用的线程安全解析器
    private static final Parser HTML_PARSER = Parser.htmlParser();

    private HtmlTextExtractor() {
    }

    /**
     * 将 HTML 字符串转为纯文本，并保留换行结构
     * 支持 <p>、<br>、div、h1~h6、li 等段落结构
     *
     * @param html HTML 内容
     * @return 提取后的纯文本（带换行）
     */
    public static String extractPlainTextWithNewlines(String html) {
        if (html == null || html.isBlank()) return "";
        Document doc = Jsoup.parse(html, "", HTML_PARSER);
        FormattingVisitor formatter = new FormattingVisitor();
        NodeTraversor.traverse(formatter, doc.body());
        return formatter.toText();
    }

    public static void main(String[] args) {
        String html = "<p><strong>代码分析：</strong></p>\n" +
                "\n" +
                "<p>这段代码<code>float f = 3.14;</code>是<strong>不合法</strong>的，无法通过编译。</p>\n" +
                "\n" +
                "<p><strong>问题原因：</strong></p>\n" +
                "<ol>\n" +
                "  <li><strong>默认浮点字面量类型：</strong> 在Java中，小数字面量（如3.14）默认被视为<code>double</code>类型，而不是<code>float</code>类型。</li>\n" +
                "  <li><strong>精度丢失问题：</strong> 将<code>double</code>类型（64位）赋值给<code>float</code>类型（32位）可能导致精度丢失，因此Java编译器不允许这种隐式转换。</li>\n" +
                "  <li><strong>编译错误：</strong> 编译器会报错：\"错误: 不兼容的类型: 从double转换到float可能会有损失\"。</li>\n" +
                "</ol>\n" +
                "\n" +
                "<p><strong>修正方法：</strong></p>\n" +
                "\n" +
                "<p>有以下几种方式可以修正这个问题：</p>\n" +
                "\n" +
                "<p>1. 使用<code>float</code>字面量后缀<code>f</code>或<code>F</code>：</p>\n" +
                "<pre><code>float f = 3.14f;  // 或 float f = 3.14F;</code></pre>\n" +
                "\n" +
                "<p>2. 使用显式类型转换：</p>\n" +
                "<pre><code>float f = (float) 3.14;</code></pre>\n" +
                "\n" +
                "<p>3. 如果确实需要高精度，考虑直接使用<code>double</code>类型：</p>\n" +
                "<pre><code>double d = 3.14;</code></pre>\n" +
                "\n" +
                "<p><strong>为什么Java这样设计：</strong></p>\n" +
                "<ul>\n" +
                "  <li><strong>历史原因：</strong> Java设计之初参考了C和C++，保留了浮点字面量默认为double的特性。</li>\n" +
                "  <li><strong>安全考虑：</strong> 这种设计可以防止意外的精度丢失，强制开发者显式声明当需要使用较低精度的float时。</li>\n" +
                "  <li><strong>浮点运算特性：</strong> 浮点运算本身就存在精度问题，Java的这种设计可以提醒开发者注意这一点。</li>\n" +
                "</ul>\n" +
                "\n" +
                "<p><strong>相关知识点：</strong></p>\n" +
                "<ul>\n" +
                "  <li>Java中浮点类型的精度：float (32位，约7位十进制精度) vs double (64位，约15-16位十进制精度)。</li>\n" +
                "  <li>所有浮点运算默认都是以double精度进行的，除非显式使用float。</li>\n" +
                "  <li>当需要高精度计算时，应考虑使用java.math.BigDecimal类。</li>\n" +
                "</ul>";

        int times = 100;

        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long totalTime = 0;

        for (int i = 0; i < times; i++) {
            long startTime = System.nanoTime();
            String result = HtmlTextExtractor.extractPlainTextWithNewlines(html);
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            totalTime += duration;
            minTime = Math.min(minTime, duration);
            maxTime = Math.max(maxTime, duration);
        }

        double averageTime = totalTime / (double) times;

        System.out.println("执行次数: " + times);
        System.out.println("最快时间: " + minTime + " ns");
        System.out.println("最慢时间: " + maxTime + " ns");
        System.out.println("平均时间: " + String.format("%.2f", averageTime) + " ns");

    }

    /**
     * 内部类：遍历节点提取文本并保留换行
     */
    private static class FormattingVisitor implements NodeVisitor {

        private static final String[] BLOCK_TAGS = {
                "p", "div", "section", "br",
                "h1", "h2", "h3", "h4", "h5", "h6",
                "ul", "ol", "li", "table", "tr"
        };
        private final StringBuilder accum = new StringBuilder(1024);

        @Override
        public void head(Node node, int depth) {
            if (node instanceof TextNode textNode) {
                // 保留原始文本内容（包括换行和空格）
                accum.append(textNode.getWholeText());
            } else if (node instanceof Element el) {
                String tag = el.normalName();
                if (isBlockTag(tag)) {
                    newline(); // 在进入块级标签前添加换行
                }
            }
        }

        @Override
        public void tail(Node node, int depth) {
            if (node instanceof Element el) {
                String tag = el.normalName();
                if (isBlockTag(tag)) {
                    newline(); // 在块级标签结束后添加换行
                }
            }
        }

        private boolean isBlockTag(String tag) {
            for (String t : BLOCK_TAGS) {
                if (t.equals(tag)) return true;
            }
            return false;
        }

        private void newline() {
            int len = accum.length();
            if (len == 0 || accum.charAt(len - 1) != '\n') {
                accum.append('\n');
            }
        }

        public String toText() {
            // 移除多余空行，规整空白
            return accum.toString()
                    .replaceAll("[ \\t\\x0B\\f\\r]+", " ")       // 空白规整
                    .replaceAll("(?m)^[ \t]*\n", "")              // 清除空白行
                    .replaceAll("\n{3,}", "\n\n")                // 连续换行只保留两行
                    .trim();
        }
    }
}