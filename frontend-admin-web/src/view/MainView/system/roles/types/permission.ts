export interface Permission {
  id: number
  name: string
  code: string
  type: 'menu' | 'button'
  parent_id?: number
  children?: Permission[]
}

export interface RolePermission {
  role_id: number
  permission_ids: number[]
  menu_ids: number[]
}

// 权限等级
export enum PermissionLevel {
  LOW = 'low',
  MEDIUM = 'medium',
  HIGH = 'high',
  SUPER = 'super'
}

// 权限模板
export interface PermissionTemplate {
  id: number
  name: string
  level: PermissionLevel
  description: string
  permissions: number[]
  menus: number[]
} 