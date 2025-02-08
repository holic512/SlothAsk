export interface AdminRole {
  id: number
  name: string
  description: string
  permissions: string
  status: number
  create_time?: string
  update_time?: string
}

export interface AdminUser {
  id?: number
  username: string
  real_name?: string
  email?: string
  phone?: string
  status: number
  role_id: number
  role_name?: string
  create_time?: string
  last_login_time?: string
  last_login_ip?: string
  login_count?: number
} 