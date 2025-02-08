import { ElMessageBox, ElMessage } from 'element-plus'
import { apiDeleteCategory } from './ApiDeleteCategory'
import { useCategoryStore } from '../pinia/categoryStore'
import type { ICategory } from '../types/types'

/**
 * 处理单个删除
 */
export const handleDelete = async (row: ICategory) => {
    try {
        await ElMessageBox.confirm(
            `确定要删除分类 "${row.name}" 吗？`,
            '删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )

        const response = await apiDeleteCategory(row.id)
        if (response.status === 200) {
            ElMessage.success('删除成功')
            // 刷新列表
            const categoryStore = useCategoryStore()
            await categoryStore.loadCategoryList()
        } else {
            ElMessage.error(response.message || '删除失败')
        }
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('删除失败')
        }
    }
} 