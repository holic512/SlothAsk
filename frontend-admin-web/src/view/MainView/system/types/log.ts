export interface AdminLoginLog {
  id?: number             // bigint id PK "自增主键"
  admin_id: number        // bigint admin_id FK "管理员ID"
  login_ip: string        // varchar login_ip "登录IP"
  login_time: string      // datetime login_time "登录时间"
  login_status: number    // tinyint login_status "1:成功 0:失败"
  user_agent: string      // varchar user_agent "浏览器信息"
  create_time?: string    // datetime create_time "创建时间"
} 