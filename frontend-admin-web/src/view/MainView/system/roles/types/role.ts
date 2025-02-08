export interface RoleInfo {
  id: number
  name: string
  description?: string
  status: number
  create_time?: string
  permissions?: string[]
  menus?: string[]
} 