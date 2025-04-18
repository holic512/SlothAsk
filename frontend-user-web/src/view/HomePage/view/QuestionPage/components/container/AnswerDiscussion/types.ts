export type SortType = 'newest' | 'oldest' | 'popular';

export interface ReplyType {
  id: number;
  userId: number;
  nickname: string;
  avatar: string;
  content: string;
  createTime: string;
  parentId: number;
}

export interface CommentType {
  id: number;
  userId: number;
  nickname: string;
  avatar: string;
  content: string;
  createTime: string;
  likeCount: number;
  isLiked: boolean;
  replies: ReplyType[];
}

export interface SortOption {
  [key: string]: string;
} 