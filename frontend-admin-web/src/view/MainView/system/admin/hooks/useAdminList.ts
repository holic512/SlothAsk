import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminList } from '../service/mockService'
import type { AdminUser, SearchParams, PaginationInfo } from '../types'

export function useAdminList() {
  const loading = ref(false)
  const adminList = ref<AdminUser[]>([])
  const pagination = ref<PaginationInfo>({
    current_page: 1,
    page_size: 10,
    total: 0
  })

  const fetchAdminList = async (params?: SearchParams) => {
    loading.value = true
    try {
      const { data } = await getAdminList({
        ...params,
        current_page: pagination.value.current_page,
        page_size: pagination.value.page_size
      })
      adminList.value = data.list
      pagination.value.total = data.total
    } catch (error) {
      ElMessage.error('获取管理员列表失败')
      console.error('获取管理员列表失败:', error)
    } finally {
      loading.value = false
    }
  }

  const handleSizeChange = (size: number) => {
    pagination.value.page_size = size
    pagination.value.current_page = 1
    fetchAdminList()
  }

  const handleCurrentChange = (page: number) => {
    pagination.value.current_page = page
    fetchAdminList()
  }

  const handleSortChange = (sort: { prop: string, order: 'ascending' | 'descending' | null }) => {
    if (sort.order) {
      fetchAdminList({
        sort_by: sort.prop,
        sort_order: sort.order
      })
    }
  }

  return {
    loading,
    adminList,
    pagination,
    fetchAdminList,
    handleSizeChange,
    handleCurrentChange,
    handleSortChange
  }
} 