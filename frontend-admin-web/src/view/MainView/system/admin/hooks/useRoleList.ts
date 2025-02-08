import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { AdminRole } from '../types'
import { getRoleList as fetchRoleList } from '../service/mockService'

export function useRoleList() {
  const roleList = ref<AdminRole[]>([])

  // 获取角色列表
  const getRoleList = async () => {
    try {
      const response = await fetchRoleList()
      if (response.code === 0) {
        roleList.value = response.data
      }
    } catch (error) {
      ElMessage.error('获取角色列表失败')
      console.error('获取角色列表失败:', error)
    }
  }

  return {
    roleList,
    getRoleList
  }
} 