import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { RoleInfo } from '../types/role'
import { createRole, updateRole } from '../api/roleService'

export function useRoleDialog() {
  const dialogVisible = ref(false)
  const dialogTitle = ref('')
  const submitLoading = ref(false)
  
  const formData = ref<Partial<RoleInfo>>({
    name: '',
    description: '',
    status: 1,
    permissions: [], // 默认权限为空
    menus: []       // 默认菜单为空
  })

  const resetForm = () => {
    formData.value = {
      name: '',
      description: '',
      status: 1,
      permissions: [],
      menus: []
    }
  }

  const openAddDialog = () => {
    dialogTitle.value = '添加角色'
    resetForm()
    dialogVisible.value = true
  }

  const openEditDialog = (row: RoleInfo) => {
    dialogTitle.value = '编辑角色'
    formData.value = { ...row }
    dialogVisible.value = true
  }

  const handleSubmit = async () => {
    submitLoading.value = true
    try {
      const api = formData.value.id ? updateRole : createRole
      const response = await api(formData.value)
      if (response.code === 0) {
        ElMessage.success(formData.value.id ? '更新成功' : '创建成功')
        dialogVisible.value = false
        return true
      } else {
        ElMessage.error(response.message || '操作失败')
        return false
      }
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('操作失败，请重试')
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