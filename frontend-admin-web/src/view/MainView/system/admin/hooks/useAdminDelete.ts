import { ElMessageBox, ElMessage } from 'element-plus'
import { deleteAdmin, batchDeleteAdmins } from '../service/mockService'
import type { AdminUser } from '../types'

export function useAdminDelete() {
  // 删除单个管理员
  const handleDelete = async (id: number): Promise<boolean> => {
    try {
      await ElMessageBox.confirm('确定要删除该管理员吗？', '提示', {
        type: 'warning'
      })
      const response = await deleteAdmin(id)
      if (response.code === 0) {
        ElMessage.success('删除成功')
        return true
      }
      return false
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('删除失败')
        console.error('删除管理员失败:', error)
      }
      return false
    }
  }

  // 批量删除管理员
  const handleBatchDelete = async (ids: number[]): Promise<boolean> => {
    try {
      await ElMessageBox.confirm(`确定要删除选中的 ${ids.length} 个管理员吗？`, '提示', {
        type: 'warning'
      })
      const response = await batchDeleteAdmins(ids)
      if (response.code === 0) {
        ElMessage.success('批量删除成功')
        return true
      }
      return false
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('批量删除失败')
        console.error('批量删除管理员失败:', error)
      }
      return false
    }
  }

  return {
    handleDelete,
    handleBatchDelete
  }
} 