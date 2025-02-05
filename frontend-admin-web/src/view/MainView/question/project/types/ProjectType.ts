export interface Project {
    id?: number;
    name: string;
    description: string;
    sortOrder: number;
    status: number;
    categoryCount?: number;
    questionCount?: number;
    createTime?: string;
    updateTime?: string;
} 