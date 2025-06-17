/**
 * 评论项接口
 */
export interface CommentItemInterface {
    /**
     * 关联的问题唯一标识
     */
    questionTUid: string;
    /**
     * 问题标题
     */
    questionTitle: string;
    /**
     * 评论内容
     */
    content: string;
    /**
     * 点赞数量
     */
    likes: number;
    /**
     * 创建时间
     */
    createdAt: string;
}