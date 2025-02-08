import { mockAdmins, mockRoles, mockPermissions, mockPagination } from '../mock'
import type { AdminUser, AdminRole, ApiResponse, PageResult, SearchParams } from '../types'
import type { Role } from '../types/role'

// 模拟延迟
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

// 模拟API响应格式
interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 模拟响应包装器
function mockResponse<T>(data: T): ApiResponse<T> {
  return {
    code: 0,
    message: 'success',
    data
  }
}

// 获取管理员列表
export async function getAdminList(params?: SearchParams): Promise<ApiResponse<PageResult<AdminUser>>> {
  await delay(500)
  let result = [...mockAdmins]

  if (params) {
    // 搜索过滤
    if (params.username) {
      result = result.filter(item => item.username.includes(params.username))
    }
    if (params.role_id) {
      result = result.filter(item => item.role_id === params.role_id)
    }
    if (params.status !== undefined) {
      result = result.filter(item => item.status === params.status)
    }

    // 排序
    if (params.sort_by && params.sort_order) {
      result.sort((a: any, b: any) => {
        return params.sort_order === 'ascending' 
          ? a[params.sort_by] > b[params.sort_by] ? 1 : -1
          : a[params.sort_by] < b[params.sort_by] ? 1 : -1
      })
    }
  }

  return {
    code: 0,
    message: 'success',
    data: {
      list: result,
      total: result.length
    }
  }
}

// 模拟添加管理员
export async function addAdmin(data: Partial<AdminUser>): Promise<ApiResponse<AdminUser>> {
  await delay(500)
  const newAdmin = {
    ...data,
    id: Math.max(...mockAdmins.map(item => item.id!)) + 1,
    create_time: new Date().toISOString(),
    update_time: new Date().toISOString(),
    login_count: 0,
    last_login_ip: '',
    last_login_time: '',
    role_name: mockRoles.find(role => role.id === data.role_id)?.name
  }
  mockAdmins.push(newAdmin as AdminUser)
  return mockResponse(newAdmin)
}

// 模拟更新管理员
export async function updateAdmin(id: number, data: Partial<AdminUser>): Promise<ApiResponse<AdminUser>> {
  await delay(500)
  const index = mockAdmins.findIndex(item => item.id === id)
  if (index > -1) {
    const updatedAdmin = {
      ...mockAdmins[index],
      ...data,
      update_time: new Date().toISOString(),
      role_name: mockRoles.find(role => role.id === data.role_id)?.name
    }
    mockAdmins[index] = updatedAdmin
    return mockResponse(updatedAdmin)
  }
  return mockResponse(null)
}

// 删除管理员
export async function deleteAdmin(id: number): Promise<ApiResponse<null>> {
  await delay(500)
  const index = mockAdmins.findIndex(item => item.id === id)
  if (index > -1) {
    mockAdmins.splice(index, 1)
    return mockResponse(null)
  }
  return mockResponse(null)
}

// 批量删除管理员
export async function batchDeleteAdmins(ids: number[]): Promise<ApiResponse<null>> {
  await delay(500)
  ids.forEach(id => {
    const index = mockAdmins.findIndex(item => item.id === id)
    if (index > -1) {
      mockAdmins.splice(index, 1)
    }
  })
  return mockResponse(null)
}

// 获取角色列表
export async function getRoleList(): Promise<ApiResponse<AdminRole[]>> {
  await delay(500)
  return mockResponse(mockRoles)
}

// 模拟获取权限树
export async function getPermissionTree() {
  await delay(500)
  return mockResponse(mockPermissions)
}

// 更新管理员状态
export async function updateAdminStatus(id: number, status: number): Promise<ApiResponse<null>> {
  await delay(500)
  const admin = mockAdmins.find(item => item.id === id)
  if (admin) {
    admin.status = status
    admin.update_time = new Date().toISOString()
    return mockResponse(null)
  }
  return mockResponse(null)
}

// 修改密码
export async function changePassword(data: {
  userId: number
  oldPassword: string
  newPassword: string
}): Promise<ApiResponse<null>> {
  await delay(500)
  // 在实际应用中，这里会调用后端 API 进行密码验证和修改
  return mockResponse(null)
} 