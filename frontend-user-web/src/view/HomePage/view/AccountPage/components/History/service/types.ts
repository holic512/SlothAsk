// 定义后端API返回的类型

// 标签简单DTO
export interface TagSimpleDTO {
  id: number;
  name: string;
}

// 用户历史记录DTO
export interface UserHistoryDTO {
  virtualId: string;      // 问题虚拟ID
  title: string;          // 问题标题
  difficulty: number;     // 难度
  categoryName: string;   // 分类名称
  visitTime: string;      // 最近访问时间
  tagCategoryIds: string; // 标签分类ID字符串
}

// API返回的历史问题DTO
export interface HistoryQuestionDTO {
  userHistoryList: UserHistoryDTO[];
  tagSimpleList: TagSimpleDTO[];
}

// API响应类型
export interface ApiResponse<T> {
  status: number;
  message: string;
  data?: T;
}

// 前端使用的历史记录类型
export interface HistoryRecord {
  id: string;
  title: string;
  visitTime: string;
  category: string;
  tags: string[];
  difficulty: string;
}

// 按日期分组的历史记录
export interface HistoryGroup {
  date: string;
  records: HistoryRecord[];
}

// 难度映射
export const difficultyMap: Record<number, string> = {
  1: '入门',
  2: '简单',
  3: '中等',
  4: '困难',
  5: '专家',
};

// 难度颜色映射
export const difficultyColors: Record<string, string> = {
  '简单': '#67C23A',
  '中等': '#E6A23C',
  '困难': '#F56C6C',
  '入门': '#909399',
  '专家': '#8B00FF'
}; 