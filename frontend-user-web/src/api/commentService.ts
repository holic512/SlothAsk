import instance from '@/axios/axios';
import type {
  CommentType,
  SortType
} from '@/view/HomePage/view/QuestionPage/components/container/AnswerDiscussion/types';

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

// 评论列表响应接口
export interface CommentListResponse {
  totalCount: number;       // 总评论数
  pageNum: number;          // 当前页码
  pageSize: number;         // 每页数量
  comments: CommentType[];  // 评论列表
}

// 评论提交参数接口
export interface CommentSubmitParams {
  targetVirtualId: string;  // 目标虚拟ID
  content: string;          // 评论内容
  parentId?: number;        // 父评论ID，用于回复功能
}

/**
 * 评论服务
 */
export const commentService = {
  /**
   * 获取评论列表
   * @param params 查询参数
   */
  async getComments(params: CommentQueryParams): Promise<CommentType[]> {
    try {
      const response = await instance.get<ApiResponse<CommentListResponse>>('/user/questionComment/list', {
        params: params
      });
      
      if (response.data.status === 200 && response.data.data) {
        return response.data.data.comments;
      }
      return [];
    } catch (error) {
      console.error('获取评论列表失败', error);
      return [];
    }
  },

  /**
   * 添加评论或回复
   * @param params 评论参数
   */
  async addComment(params: CommentSubmitParams): Promise<boolean> {
    try {
      const response = await instance.post<ApiResponse>('/user/questionComment/add', params);
      return response.data.status === 200;
    } catch (error) {
      console.error('添加评论失败', error);
      return false;
    }
  },

  /**
   * 点赞评论
   * @param virtualId 评论虚拟ID
   */
  async likeComment(virtualId: string): Promise<boolean> {
    try {
      const response = await instance.post<ApiResponse>(`/user/questionComment/like/${virtualId}`);
      return response.data.status === 200;
    } catch (error) {
      console.error('点赞评论失败', error);
      return false;
    }
  },

  /**
   * 取消点赞评论
   * @param virtualId 评论虚拟ID
   */
  async unlikeComment(virtualId: string): Promise<boolean> {
    try {
      const response = await instance.post<ApiResponse>(`/user/questionComment/unlike/${virtualId}`);
      return response.data.status === 200;
    } catch (error) {
      console.error('取消点赞评论失败', error);
      return false;
    }
  }
}; 