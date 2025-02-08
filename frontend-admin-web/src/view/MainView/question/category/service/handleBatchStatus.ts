import axios from "@/axios/axios"
import { ElMessage } from 'element-plus'
import { useCategoryStore } from '../pinia/categoryStore'
import { handleSearch } from './handleSearch'

/**
 * 批量更新分类状态
 */
export const handleBatchStatus = async (ids: number[], status: 0 | 1) => {
  const categoryStore = useCategoryStore()
  
  try {
    const response = await axios.put('/admin/category/batch/status', {
      ids,
      status
    })
    
    if (response.data.status === 200) {
      ElMessage.success('状态更新成功')
      // 重新加载列表
      await handleSearch({
        keyword: categoryStore.searchKeyword,
        projectId: categoryStore.searchProjectId,
        status: categoryStore.searchStatus,
        pageNum: categoryStore.pageNum,
        pageSize: categoryStore.pageSize
      })
      // 清空选中
      categoryStore.selectedRows = []
      return true
    } else {
      ElMessage.error(response.data.message || '状态更新失败')
      return false
    }
  } catch (error) {
    console.error('更新分类状态失败:', error)
    ElMessage.error('状态更新失败')
    return false
  }
} 