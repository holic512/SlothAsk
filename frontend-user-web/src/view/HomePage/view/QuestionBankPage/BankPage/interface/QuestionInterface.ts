interface Question {
    id: number;
    categoryId: number;
    title: string;
    content: string;
    answer: string | string[] | boolean;
    difficulty: number; // 1:简单 2:中等 3:困难
    type: number; // 1:单选 2:多选 3:判断 4:简答
    tagCategoryIds: Array<number>;
    status: number; // 1:正常 0:禁用
    view_count: number;
    create_time: string;
    update_time: string;
}