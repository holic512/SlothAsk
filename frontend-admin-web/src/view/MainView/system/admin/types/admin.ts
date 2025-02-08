import type { DataScopeType } from '../../roles/types/role'

export interface AdminUser {
  id?: number                // bigint id PK "自增主键"
  username: string          // varchar username "用户名"
  password?: string         // varchar password "密码(加密存储)"
  real_name: string         // varchar real_name "真实姓名"
  email: string            // varchar email "邮箱"
  phone: string            // varchar phone "手机号"
  role_id: number          // bigint role_id FK "角色ID"
  role_name: string
  status: number           // tinyint status "1:正常 0:禁用"
  data_scope?: DataScopeType
  permissions?: string[]
  create_time?: string     // datetime create_time "创建时间"
  update_time?: string     // datetime update_time "更新时间"
  last_login_ip?: string   // varchar last_login_ip "最后登录IP"
  last_login_time?: string // datetime last_login_time "最后登录时间"
  login_count?: number     // int login_count "登录次数"
} 