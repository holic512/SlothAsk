import { ref } from 'vue'
import type { RoleInfo } from '../types/role'
import { getRoleList } from '../api/roleService'

export function useRoleList() {
  const loading = ref(false)
  const roleList = ref<RoleInfo[]>([])
  const pagination = ref({
    current_page: 1,
    page_size: 10,
    total: 0
  })

  const fetchRoleList = async (params = {}) => {
    loading.value = true
    try {
      const { data } = await getRoleList({
        page: pagination.value.current_page,
        pageSize: pagination.value.page_size,
        ...params
      })
      roleList.value = data.list
      pagination.value.total = data.total
    } catch (error) {
      console.error('获取角色列表失败:', error)
    } finally {
      loading.value = false
    }
  }

  const handleSizeChange = (size: number) => {
    pagination.value.page_size = size
    fetchRoleList()
  }

  const handleCurrentChange = (page: number) => {
    pagination.value.current_page = page
    fetchRoleList()
  }

  const handleSortChange = ({ prop, order }: { prop: string, order: string }) => {
    fetchRoleList({
      sortField: prop,
      sortOrder: order
    })
  }

  return {
    loading,
    roleList,
    pagination,
    fetchRoleList,
    handleSizeChange,
    handleCurrentChange,
    handleSortChange
  }
} 