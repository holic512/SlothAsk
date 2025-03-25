export interface QuestionListInterface {
    currentIndex: number;
    totalCount: number;
    currentPage: number;
    totalPages: number;
    pageSize: number;
    categoryId: number;
    categoryName: string;
    questions: QuestionBriefInterface[];
}

export interface QuestionBriefInterface {
    id: number;
    virtualId: string;
    title: string;
    type: number;
    difficulty: number;
    current: boolean;
} 