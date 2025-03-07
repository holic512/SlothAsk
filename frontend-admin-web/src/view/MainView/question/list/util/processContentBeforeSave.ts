export const processContentBeforeSave = (html: string) => {
    try {
        // 创建 DOM 解析器 并 将 HTML 字符串解析为 DOM 文档对象
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');

        // 选择已有 alt 的图片组
        const images = doc.querySelectorAll('img[alt]');

        images.forEach((img) => {
            const alt = img.getAttribute('alt') || ''; // 避免 null 影响

            if (alt.startsWith("#")) {
                img.removeAttribute('src'); // 直接移除 src 而不是设为空
                img.removeAttribute('data-href'); // 移除不必要的属性
            }
        });

        return doc.body.innerHTML; // 返回优化后的 HTML
    } catch (error) {
        console.error("processContentBeforeSave 解析 HTML 失败:", error);
        return html; // 遇到错误返回原 HTML，避免程序崩溃
    }
};
