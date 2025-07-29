const BASE_TITLE = '树懒问答(SlothAsk)';
const TITLE_SUFFIX = ` - ${BASE_TITLE}`;

export const setTitle = (pageTitle?: string) => {
    const newTitle = pageTitle ? pageTitle + TITLE_SUFFIX : BASE_TITLE;
    // 避免重复设置相同标题
    if (document.title !== newTitle) {
        document.title = newTitle;
    }
};