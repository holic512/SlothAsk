import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { addAdmin, updateAdmin } from '../service/mockService'
import type { AdminUser } from '../types'

export function useAdminDialog() {
  const dialogVisible = ref(false)
  const dialogTitle = ref<string>('添加管理员')
  const formData = ref<Partial<AdminUser>>({
    status: 1
  })
  const submitLoading = ref(false)

  // 打开添加对话框
  const openAddDialog = () => {
    dialogTitle.value = '添加管理员'
    formData.value = {
      status: 1
    }
    dialogVisible.value = true
  }

  // 打开编辑对话框
  const openEditDialog = (row: AdminUser) => {
    dialogTitle.value = '编辑管理员'
    formData.value = { ...row }
    dialogVisible.value = true
  }

  // 提交表单
  const handleSubmit = async (): Promise<boolean> => {
    submitLoading.value = true
    try {
      const isEdit = formData.value.id !== undefined
      const response = isEdit 
        ? await updateAdmin(formData.value.id!, formData.value)
        : await addAdmin(formData.value)
      
      if (response.code === 0) {
        ElMessage.success(isEdit ? '更新成功' : '添加成功')
        dialogVisible.value = false
        return true
      }
      ElMessage.error(response.message || '操作失败')
      return false
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('操作失败')
      return false
    } finally {
      submitLoading.value = false
    }
  }

  return {
    dialogVisible,
    dialogTitle,
    formData,
    submitLoading,
    openAddDialog,
    openEditDialog,
    handleSubmit
  }
} 