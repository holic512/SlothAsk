export interface AdminRole {
  id: number               // bigint id PK "自增主键"
  name: string            // varchar name "角色名称"
  description: string     // varchar description "角色描述"
  permissions: string     // text permissions "权限集合(JSON)"
  status: number          // tinyint status "1:正常 0:禁用"
  create_time?: string    // datetime create_time "创建时间"
  update_time?: string    // datetime update_time "更新时间"
} 