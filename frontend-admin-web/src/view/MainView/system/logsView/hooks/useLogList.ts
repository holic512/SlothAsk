import { ref, computed } from 'vue'
import { mockAdmins, mockLoginLogs } from '../../admin/mock'
import type { AdminLoginLog } from '../../types/log'

export function useLogList() {
  const searchQuery = ref('')
  const pagination = ref({
    current_page: 1,
    page_size: 10
  })

  const filteredLogs = computed(() => {
    return mockLoginLogs
      .map(log => {
        const user = mockAdmins.find(user => user.id === log.admin_id)
        return {
          ...log,
          username: user ? user.username : '未知用户'
        }
      })
      .filter(log => log.username.includes(searchQuery.value))
  })

  const paginatedLogs = computed(() => {
    const start = (pagination.value.current_page - 1) * pagination.value.page_size
    const end = start + pagination.value.page_size
    return filteredLogs.value.slice(start, end)
  })

  return {
    logs: paginatedLogs,
    searchQuery,
    pagination
  }
} 