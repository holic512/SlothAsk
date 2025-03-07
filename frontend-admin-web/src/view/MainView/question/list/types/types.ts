// 题目类型定义
export interface IQuestion {
    id: number
    projectId: number | undefined
    categoryId: number | undefined
    title: string | undefined
    content: string | undefined
    answer: string | undefined
    difficulty: number | undefined
    type: number | undefined
    tagCategoryIds: number[] | undefined
    status: number | undefined
    viewCount: number | undefined | null
    createTime: string | undefined | null
    updateTime: string | undefined
}

// 搜索参数类型
export interface IQuestionSearchParams {
    pageNum: number
    pageSize: number
    keyword?: string
    projectId?: number
    categoryId?: number
    difficulty?: number
    type?: number
    // tagCategoryId?: number
    status?: number
    startTime?: string
    endTime?: string
}

// 题目表单类型
export interface IQuestionForm {
    projectId: number | undefined
    categoryId: number | undefined
    tagCategoryId: number[] | undefined
    title: string
    content: string
    answer: string
    difficulty: number
    type: number
    status: number
}

// 项目列表
export interface IProjectList {
    id: number
    name: string
}

// 分类列表
export interface ICategoryList {
    id: number
    name: string
}

// 标签列表
export interface ITagList {
    id: number
    name: string
}