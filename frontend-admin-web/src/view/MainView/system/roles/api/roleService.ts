import type { RoleInfo } from '../types/role'
import { mockRoleList, mockPermissionTree, mockRolePermissions } from '../../mock'

interface RoleListParams {
  page?: number
  pageSize?: number
  name?: string
  status?: number
  sortField?: string
  sortOrder?: string
}

interface RoleListResponse {
  code: number
  data: {
    list: RoleInfo[]
    total: number
  }
}

interface BaseResponse {
  code: number
  message?: string
}

interface PermissionTree {
  id: number
  name: string
  children?: PermissionTree[]
}

interface PermissionTreeResponse {
  code: number
  data: {
    tree: PermissionTree[]
    checkedKeys: number[]
  }
}

// 获取角色列表
export const getRoleList = async (params: RoleListParams): Promise<RoleListResponse> => {
  try {
    // 模拟API调用
    const { page = 1, pageSize = 10, name, status } = params
    
    let filteredList = [...mockRoleList]
    
    // 搜索过滤
    if (name) {
      filteredList = filteredList.filter(item => 
        item.name.toLowerCase().includes(name.toLowerCase())
      )
    }
    
    if (status !== undefined) {
      filteredList = filteredList.filter(item => item.status === status)
    }
    
    // 排序
    if (params.sortField) {
      const order = params.sortOrder === 'ascending' ? 1 : -1
      filteredList.sort((a: any, b: any) => {
        if (a[params.sortField!] < b[params.sortField!]) return -1 * order
        if (a[params.sortField!] > b[params.sortField!]) return 1 * order
        return 0
      })
    }
    
    // 分页
    const total = filteredList.length
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const list = filteredList.slice(start, end)
    
    return {
      code: 0,
      data: {
        list,
        total
      }
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
    return {
      code: -1,
      data: {
        list: [],
        total: 0
      }
    }
  }
}

// 创建角色
export const createRole = async (data: Partial<RoleInfo>): Promise<BaseResponse> => {
  // 模拟创建角色
  const newRole: RoleInfo = {
    id: mockRoleList.length + 1,
    name: data.name!,
    description: data.description!,
    status: data.status!,
    create_time: new Date().toISOString().slice(0, 19).replace('T', ' '),
    permissions: [],
    menus: []
  }

  // 添加到 mock 数据
  mockRoleList.push(newRole)

  // 初始化角色权限
  mockRolePermissions[newRole.id] = {
    role_id: newRole.id,
    permission_ids: [], // 新角色默认无权限
    menu_ids: []       // 新角色默认无菜单
  }

  return {
    code: 0,
    message: '创建成功'
  }
}

// 更新角色
export const updateRole = async (data: Partial<RoleInfo>): Promise<BaseResponse> => {
  return {
    code: 0,
    message: '更新成功'
  }
}

// 删除角色
export const deleteRole = async (id: number): Promise<BaseResponse> => {
  return {
    code: 0,
    message: '删除成功'
  }
}

// 获取权限树
export const getPermissionTree = async (roleId: number): Promise<PermissionTreeResponse> => {
  // 从 mock 数据中获取角色权限
  const rolePermission = mockRolePermissions[roleId]
  
  return {
    code: 0,
    data: {
      tree: mockPermissionTree,
      checkedKeys: rolePermission?.permission_ids || []
    }
  }
}

// 更新角色权限
export const updateRolePermissions = async (data: {
  roleId: number
  permissions: number[]
}): Promise<BaseResponse> => {
  try {
    // 在实际应用中，这里会发送请求到后端
    // 这里我们只是模拟更新
    if (data.roleId === 1) {
      return {
        code: -1,
        message: '超级管理员权限不可修改'
      }
    }
    
    // 更新 mock 数据
    mockRolePermissions[data.roleId] = {
      ...mockRolePermissions[data.roleId],
      permission_ids: data.permissions
    }
    
    return {
      code: 0,
      message: '权限更新成功'
    }
  } catch (error) {
    console.error('更新权限失败:', error)
    return {
      code: -1,
      message: '更新失败'
    }
  }
} 