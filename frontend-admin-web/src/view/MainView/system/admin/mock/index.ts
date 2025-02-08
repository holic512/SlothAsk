import type { AdminUser, AdminRole } from '../types'

// 模拟分页配置
export const mockPagination = {
  current_page: 1,
  page_size: 10,
  total: 0
}

// 模拟角色数据
export const mockRoles: AdminRole[] = [
  {
    id: 1,
    name: '超级管理员',
    description: '系统最高权限',
    permissions: JSON.stringify(['*']),
    status: 1,
    create_time: '2024-01-01 00s:00:00',
    update_time: '2024-01-01 00:00:00'
  },
  {
    id: 2,
    name: '普通管理员',
    description: '一般管理权限',
    permissions: JSON.stringify(['user:view', 'user:edit']),
    status: 1,
    create_time: '2024-01-01 00:00:00',
    update_time: '2024-01-01 00:00:00'
  }
]

// 模拟管理员数据
export const mockAdmins: AdminUser[] = [
  {
    id: 1,
    username: 'admin',
    real_name: '系统管理员',
    email: 'admin@example.com',
    phone: '13800138000',
    role_id: 1,
    last_login_ip: '127.0.0.1',
    last_login_time: '2024-01-01 12:00:00',
    login_count: 100,
    status: 1,
    create_time: '2024-01-01 00:00:00',
    update_time: '2024-01-01 00:00:00'
  },
  {
    id: 2,
    username: 'test',
    real_name: '测试账号',
    email: 'test@example.com',
    phone: '13800138001',
    role_id: 2,
    last_login_ip: '127.0.0.1',
    last_login_time: '2024-01-01 12:00:00',
    login_count: 50,
    status: 1,
    create_time: '2024-01-01 00:00:00',
    update_time: '2024-01-01 00:00:00'
  }
]

// 模拟权限数据
export const mockPermissions = [
  {
    id: 1,
    name: '系统管理',
    code: 'system',
    children: [
      {
        id: 11,
        name: '用户管理',
        code: 'system:user',
        children: [
          {
            id: 111,
            name: '查看用户',
            code: 'system:user:view'
          },
          {
            id: 112,
            name: '编辑用户',
            code: 'system:user:edit'
          },
          {
            id: 113,
            name: '删除用户',
            code: 'system:user:delete'
          }
        ]
      },
      {
        id: 12,
        name: '角色管理',
        code: 'system:role',
        children: [
          {
            id: 121,
            name: '查看角色',
            code: 'system:role:view'
          },
          {
            id: 122,
            name: '编辑角色',
            code: 'system:role:edit'
          },
          {
            id: 123,
            name: '删除角色',
            code: 'system:role:delete'
          }
        ]
      }
    ]
  }
]

// 保留一个有效的 `mockLoginLogs` 声明
export const mockLoginLogs = [
  { id: 1, admin_id: 1, login_ip: '192.168.1.1', login_time: '2024-03-15 08:30:00', login_status: 1, user_agent: 'Chrome on Windows 10' },
  { id: 2, admin_id: 2, login_ip: '192.168.1.2', login_time: '2024-03-14 17:45:00', login_status: 1, user_agent: 'Firefox on macOS' },
  { id: 3, admin_id: 3, login_ip: '192.168.1.3', login_time: '2024-03-13 09:15:00', login_status: 0, user_agent: 'Safari on iOS' }
]

// 模拟权限常量
export const PERMISSIONS = {
  SYSTEM: {
    USER: {
      VIEW: 'system:user:view',
      ADD: 'system:user:add',
      EDIT: 'system:user:edit',
      DELETE: 'system:user:delete'
    },
    ROLE: {
      VIEW: 'system:role:view',
      ADD: 'system:role:add',
      EDIT: 'system:role:edit',
      DELETE: 'system:role:delete'
    }
  }
}

export const mockAdminUsers = [
  { id: 1, username: 'admin', real_name: '超级管理员', email: 'admin@example.com', phone: '13800138000', role_id: 1, status: 1 },
  { id: 2, username: 'operator', real_name: '运营主管', email: 'operator@example.com', phone: '13800138001', role_id: 2, status: 1 },
  { id: 3, username: 'editor', real_name: '内容编辑', email: 'editor@example.com', phone: '13800138002', role_id: 3, status: 1 }
] 