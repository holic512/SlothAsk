export interface QuestionInterface {
    id: number | null;
    projectId: number | null;
    categoryId: number;
    categoryName?: string;
    title: string;
    content: string | null;
    answer: string | null;
    difficulty: number;
    type: number;
    tagCategoryIds: number[];
    tags?: Tag[];
    status: number;
    viewCount: number;
    createTime: string;  // ISO 时间字符串
    updateTime: string;  // ISO 时间字符串
    virtualId: string;
}

export interface Tag {
    id: number;
    name: string;
}
