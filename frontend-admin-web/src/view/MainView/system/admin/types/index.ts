// 管理员表 (admin_user)
export interface AdminUser {
  id?: number                // bigint id PK "自增主键"
  username: string          // varchar username "用户名"
  password?: string         // varchar password "密码(加密存储)"
  real_name: string         // varchar real_name "真实姓名"
  email: string            // varchar email "邮箱"
  phone: string            // varchar phone "手机号"
  role_id: number          // bigint role_id FK "角色ID"
  last_login_ip?: string   // varchar last_login_ip "最后登录IP"
  last_login_time?: string // datetime last_login_time "最后登录时间"
  login_count?: number     // int login_count "登录次数"
  status: number           // tinyint status "1:正常 0:禁用"
  create_time?: string     // datetime create_time "创建时间"
  update_time?: string     // datetime update_time "更新时间"
}

// 管理员角色表 (admin_role)
export interface AdminRole {
  id: number               // bigint id PK "自增主键"
  name: string            // varchar name "角色名称"
  description: string     // varchar description "角色描述"
  permissions: string     // text permissions "权限集合(JSON)"
  status: number          // tinyint status "1:正常 0:禁用"
  create_time?: string    // datetime create_time "创建时间"
  update_time?: string    // datetime update_time "更新时间"
}

// 管理员登录日志表 (admin_login_log)
export interface AdminLoginLog {
  id?: number             // bigint id PK "自增主键"
  admin_id: number        // bigint admin_id FK "管理员ID"
  login_ip: string        // varchar login_ip "登录IP"
  login_time: string      // datetime login_time "登录时间"
  login_status: number    // tinyint login_status "1:成功 0:失败"
  user_agent: string      // varchar user_agent "浏览器信息"
  create_time?: string    // datetime create_time "创建时间"
}

// 分页参数接口
export interface PaginationInfo {
  current_page: number
  page_size: number
  total: number
}

// 搜索参数接口
export interface SearchParams {
  username?: string
  real_name?: string
  role_id?: number
  status?: number
  sort_by?: string
  sort_order?: 'ascending' | 'descending'
}

// API响应接口
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 分页响应数据接口
export interface PageResult<T> {
  list: T[]
  total: number
}

// 状态枚举
export enum Status {
  Disabled = 0,  // 禁用
  Enabled = 1    // 正常
}

// 登录状态枚举
export enum LoginStatus {
  Failed = 0,    // 失败
  Success = 1    // 成功
}

export * from './admin'
export * from './role' 