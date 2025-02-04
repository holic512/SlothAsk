export interface TagCategory {
    id: number | undefined
    projectId: number | null
    name: string
    description: string | null
    sortOrder: number
    status: 0 | 1
    updateTime: string
} 