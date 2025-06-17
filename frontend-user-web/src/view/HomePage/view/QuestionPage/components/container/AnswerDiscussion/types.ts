export type SortType = 'newest' | 'oldest' | 'popular';

export interface UserInfo {
  id: number;
  username:string;
  nickname: string;
  avatar: string;
  isAuthor?: number; // 是否是作者
}

export interface ReplyToUser {
  id: number;
  nickname: string;
  isAuthor: number; // 0表示不是作者，1表示是作者
}

export interface ReplyType {
  id: number;
  content: string;
  parentId: number;
  likeCount: number;
  createTime: string;
  userInfo: UserInfo;
  isLike: number;  // 0表示未点赞，1表示已点赞
  isAuthor: number; // 0表示不是作者，1表示是作者
  replyToUser?: ReplyToUser; // 被回复的用户信息
  replyToUserId?: number;    // 被回复的用户ID，用于API请求
  children?: ReplyType[];    // 子回复列表，用于嵌套展示
}

export interface CommentType {
  id: number;
  content: string;
  parentId: number | null;
  likeCount: number;
  createTime: string;
  userInfo: UserInfo;
  isLike: number;  // 0表示未点赞，1表示已点赞
  isAuthor: number; // 0表示不是作者，1表示是作者
  replies: ReplyType[]; // 回复列表
  replyToUser?: ReplyToUser; // 新增：与ReplyType保持一致
  replyToUserId?: number;    // 新增：与ReplyType保持一致
}

export interface SortOption {
  [key: string]: string;
}

export interface CommentResponseData {
  total: number;
  totalPages: number;
  currentPage: number;
  comments: CommentType[];
}

export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data?: T;
} 