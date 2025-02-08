// 通用搜索参数接口
export interface SearchParams {
  currentPage?: number
  pageSize?: number
  sort_by?: string
  sort_order?: 'ascending' | 'descending'
  [key: string]: any
}

// 通用分页数据接口
export interface Pagination {
  current_page: number
  page_size: number
  total: number
}

// 通用响应接口
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 通用列表响应接口
export interface ListResponse<T> {
  list: T[]
  pagination: Pagination
}

export * from './admin'
export * from './log' 