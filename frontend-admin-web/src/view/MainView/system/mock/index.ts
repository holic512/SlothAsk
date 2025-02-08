import type { AdminUser } from '../types/admin'
import type { RoleInfo } from '../roles/types/role'
import { PermissionLevel } from '../roles/types/permission'
import type { Permission, RolePermission, PermissionTemplate } from '../roles/types/permission'
import type { AdminLoginLog } from '../types/log'
import { AdminUser as AdminUserType, AdminRole } from '../types/admin'

// 角色列表数据
export const mockRoleList: RoleInfo[] = [
  {
    id: 1,
    name: '超级管理员',
    description: '系统最高权限，可以操作所有功能',
    status: 1,
    create_time: '2024-01-01 12:00:00'
  },
  {
    id: 2,
    name: '运营管理员',
    description: '负责日常运营管理，可以操作大部分功能',
    status: 1,
    create_time: '2024-01-02 12:00:00'
  },
  {
    id: 3,
    name: '内容编辑',
    description: '负责内容管理，仅可操作内容相关功能',
    status: 1,
    create_time: '2024-01-03 12:00:00'
  },
  {
    id: 4,
    name: '访客',
    description: '仅有查看权限',
    status: 0,
    create_time: '2024-01-04 12:00:00'
  },
  {
    id: 5,
    name: '测试',
    description: '测试',
    status: 1,
    create_time: '2024-01-05 12:00:00'
  }
]


// 管理员列表数据
export const mockAdminUsers: AdminUserType[] = [
  { id: 1, username: 'admin', real_name: '超级管理员', email: 'admin@example.com', phone: '13800138000', role_id: 1, status: 1 },
  { id: 2, username: 'operator', real_name: '运营主管', email: 'operator@example.com', phone: '13800138001', role_id: 2, status: 1 },
  { id: 3, username: 'editor', real_name: '内容编辑', email: 'editor@example.com', phone: '13800138002', role_id: 3, status: 1 }
]

// 权限树数据
export const mockPermissionTree = [
  {
    id: 1,
    name: '系统管理',
    code: 'system',
    type: 'menu',
    children: [
      {
        id: 101,
        name: '用户管理',
        code: 'system:user',
        type: 'menu',
        children: [
          { id: 10101, name: '查看用户', code: 'system:user:view', type: 'button' },
          { id: 10102, name: '创建用户', code: 'system:user:create', type: 'button' },
          { id: 10103, name: '编辑用户', code: 'system:user:edit', type: 'button' },
          { id: 10104, name: '删除用户', code: 'system:user:delete', type: 'button' }
        ]
      },
      {
        id: 102,
        name: '角色管理',
        code: 'system:role',
        type: 'menu',
        children: [
          { id: 10201, name: '查看角色', code: 'system:role:view', type: 'button' },
          { id: 10202, name: '创建角色', code: 'system:role:create', type: 'button' },
          { id: 10203, name: '编辑角色', code: 'system:role:edit', type: 'button' },
          { id: 10204, name: '删除角色', code: 'system:role:delete', type: 'button' },
          { id: 10205, name: '配置权限', code: 'system:role:permission', type: 'button' }
        ]
      }
    ]
  },
  {
    id: 2,
    name: '内容管理',
    code: 'content',
    type: 'menu',
    children: [
      {
        id: 201,
        name: '文章管理',
        code: 'content:article',
        type: 'menu',
        children: [
          { id: 20101, name: '查看文章', code: 'content:article:view', type: 'button' },
          { id: 20102, name: '创建文章', code: 'content:article:create', type: 'button' },
          { id: 20103, name: '编辑文章', code: 'content:article:edit', type: 'button' },
          { id: 20104, name: '删除文章', code: 'content:article:delete', type: 'button' }
        ]
      }
    ]
  }
] as Permission[]

// 角色的默认权限配置
export const mockRolePermissions: Record<number, RolePermission> = {
  1: { // 超级管理员
    role_id: 1,
    permission_ids: [10101, 10102, 10103, 10104, 10201, 10202, 10203, 10204, 10205, 20101, 20102, 20103, 20104],
    menu_ids: [1, 101, 102, 2, 201]
  },
  2: { // 运营管理员
    role_id: 2,
    permission_ids: [10101, 10102, 10103, 20101, 20102, 20103],
    menu_ids: [1, 101, 2, 201]
  },
  3: { // 内容编辑
    role_id: 3,
    permission_ids: [20101, 20102, 20103],
    menu_ids: [2, 201]
  },
  4: { // 访客
    role_id: 4,
    permission_ids: [10101, 20101],
    menu_ids: [1, 101, 2, 201]
  },
  5: { // 测试
    role_id: 5,
    permission_ids: [10101, 10102, 10103, 10104, 10201, 10202, 10203, 10204, 10205, 20101, 20102, 20103, 20104],
    menu_ids: [1, 101, 102, 2, 201]
  }
}


// 权限模板
export const mockPermissionTemplates: PermissionTemplate[] = [
  {
    id: 1,
    name: '超级管理员模板',
    level: PermissionLevel.SUPER,
    description: '拥有所有权限',
    permissions: [10101, 10102, 10103, 10104, 10201, 10202, 10203, 10204, 10205, 20101, 20102, 20103, 20104],
    menus: [1, 101, 102, 2, 201]
  },
  {
    id: 2,
    name: '运营管理模板',
    level: PermissionLevel.HIGH,
    description: '拥有大部分管理权限',
    permissions: [10101, 10102, 10103, 20101, 20102, 20103],
    menus: [1, 101, 2, 201]
  },
  {
    id: 3,
    name: '内容编辑模板',
    level: PermissionLevel.MEDIUM,
    description: '拥有内容管理权限',
    permissions: [20101, 20102, 20103],
    menus: [2, 201]
  },
  {
    id: 4,
    name: '只读模板',
    level: PermissionLevel.LOW,
    description: '仅有查看权限',
    permissions: [10101, 20101],
    menus: [1, 101, 2, 201]
  }
]

export const mockLoginLogs: AdminLoginLog[] = [
  { id: 1, admin_id: 1, login_ip: '192.168.1.1', login_time: '2024-03-15 08:30:00', login_status: 1, user_agent: 'Chrome on Windows 10' },
  { id: 2, admin_id: 2, login_ip: '192.168.1.2', login_time: '2024-03-14 17:45:00', login_status: 1, user_agent: 'Firefox on macOS' },
  { id: 3, admin_id: 3, login_ip: '192.168.1.3', login_time: '2024-03-13 09:15:00', login_status: 0, user_agent: 'Safari on iOS' }
]

export const mockAdminRoles: AdminRole[] = [
  { id: 1, name: '超级管理员', description: '系统最高权限', permissions: '[]', status: 1 },
  { id: 2, name: '运营主管', description: '负责日常运营', permissions: '[]', status: 1 },
  { id: 3, name: '内容编辑', description: '负责内容管理', permissions: '[]', status: 1 }
] 