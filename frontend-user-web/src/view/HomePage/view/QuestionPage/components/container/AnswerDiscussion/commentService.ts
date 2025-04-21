import instance from '@/axios/axios';
import type {CommentResponseData, SortType} from './types';

// 定义API响应接口
interface ApiResponse<T = any> {
    status: number;
    message: string;
    data?: T;
}

// 评论查询参数接口
export interface CommentQueryParams {
    targetVirtualId: string;  // 目标虚拟ID
    pageNum?: number;         // 页码
    pageSize?: number;        // 每页数量
    sort?: SortType;          // 排序方式
}

// 评论提交参数接口
export interface CommentSubmitParams {
    targetVirtualId: string;  // 目标虚拟ID
    content: string;          // 评论内容
    parentId?: number;        // 父评论ID，用于回复功能
    replyToUserId?: number;   // 被回复的用户ID
}

/**
 * 评论服务
 */
export const commentService = {
    /**
     * 获取评论列表
     * @param params 查询参数
     */
    async getComments(params: CommentQueryParams): Promise<CommentResponseData> {
        try {
            const response = await instance.get<ApiResponse<CommentResponseData>>('service-question/user/questionComment/list', {
                params: params
            });
            
            if (response.data.status === 200 && response.data.data) {
                return response.data.data;
            }
            return { total: 0, totalPages: 0, currentPage: 1, comments: [] };
        } catch (error) {
            console.error('获取评论列表失败', error);
            return { total: 0, totalPages: 0, currentPage: 1, comments: [] };
        }
    },

    /**
     * 添加评论或回复
     * @param params 评论参数
     */
    async addComment(params: CommentSubmitParams): Promise<boolean> {
        try {
            const response = await instance.post<ApiResponse>('service-question/user/questionComment/add', params);
            return response.data.status === 200;
        } catch (error) {
            console.error('添加评论失败', error);
            return false;
        }
    },

    /**
     * 点赞评论
     * @param commentId 评论ID
     */
    async likeComment(commentId: number): Promise<boolean> {
        try {
            const response = await instance.post<ApiResponse>(`service-question/user/questionComment/like/${commentId}`);
            return response.data.status === 200;
        } catch (error) {
            console.error('点赞评论失败', error);
            return false;
        }
    },

    /**
     * 取消点赞评论
     * @param commentId 评论ID
     */
    async unlikeComment(commentId: number): Promise<boolean> {
        try {
            const response = await instance.post<ApiResponse>(`service-question/user/questionComment/unlike/${commentId}`);
            return response.data.status === 200;
        } catch (error) {
            console.error('取消点赞评论失败', error);
            return false;
        }
    },

    /**
     * 删除评论
     * @param commentId 评论ID
     */
    async deleteComment(commentId: number): Promise<boolean> {
        try {
            const response = await instance.post<ApiResponse>(`service-question/user/questionComment/delete/${commentId}`);
            return response.data.status === 200;
        } catch (error) {
            console.error('删除评论失败', error);
            return false;
        }
    }
}; 