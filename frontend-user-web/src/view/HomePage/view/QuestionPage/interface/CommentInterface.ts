export interface CommentInterface {
    id: number
    questionId: number
    userId: number
    likeCount: number
    nickname: string
    avatar: string
    content: string
    createTime: string
    parentId?: number | null
    replies?: CommentInterface[]
    like?: boolean
}