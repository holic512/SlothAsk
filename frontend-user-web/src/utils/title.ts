const BASE_TITLE = '树懒问答(SlothAsk) - 碎片知识整理的高效神器';

export const setTitle = (pageTitle?: string) => {
    document.title = pageTitle ? `${pageTitle} - ${BASE_TITLE}` : BASE_TITLE;
}; 