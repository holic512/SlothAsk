import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserListStore } from "../pinia/userListStore"

/**
 * 处理批量删除用户操作
 * 删除当前选中的所有用户
 */
export const handleBatchDelete = async (): Promise<void> => {
    const userListStore = useUserListStore()
    
    if (userListStore.selectedRows.length === 0) return

    try {
        await ElMessageBox.confirm(
            `确认删除选中的 ${userListStore.selectedRows.length} 个用户吗？`,
            '删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        // TODO: 调用批量删除 API
        ElMessage.success('删除成功')
    } catch {
        // 用户取消删除
    }
} 