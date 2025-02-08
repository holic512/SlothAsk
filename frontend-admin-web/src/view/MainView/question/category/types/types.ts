// 分类数据接口
export interface ICategory {
    id: number
    projectId: number
    name: string
    description: string | null
    creatorId: number
    avatarUrl: string | null
    sortOrder: number
    viewCount: number
    status: 0 | 1
    createTime: string
    updateTime: string
}

// 搜索参数接口
export interface ISearchParams {
    keyword?: string
    projectId?: number | null
    status?: number | null
    pageNum: number
    pageSize: number
}

// 表单数据接口
export interface ICategoryForm {
    id?: number
    projectId: number | null
    name: string
    description: string | null
    avatarUrl: string | null
    sortOrder: number
    status: 0 | 1
} 