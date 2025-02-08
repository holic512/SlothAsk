import { ElMessageBox, ElMessage } from 'element-plus'
import { apiBatchDeleteCategories } from './ApiDeleteCategory'
import { useCategoryStore } from '../pinia/categoryStore'

/**
 * 处理批量删除
 */
export const handleBatchDelete = async (ids: number[]) => {
    if (!ids || ids.length === 0) {
        ElMessage.warning('请选择要删除的分类')
        return
    }

    try {
        await ElMessageBox.confirm(
            `确定要删除选中的 ${ids.length} 个分类吗？`,
            '批量删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )

        const response = await apiBatchDeleteCategories(ids)
        if (response.status === 200) {
            ElMessage.success('批量删除成功')
            // 刷新列表
            const categoryStore = useCategoryStore()
            await categoryStore.loadCategoryList()
            // 清空选中
            categoryStore.selectedRows = []
        } else {
            ElMessage.error(response.message || '批量删除失败')
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error('批量删除失败:', error)
            ElMessage.error('批量删除失败')
        }
    }
} 