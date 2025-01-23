import type { IStatusOption } from '../types/search'

/**
 * 用户状态选项
 * 用于搜索表单的状态下拉选择
 */
export const statusOptions: IStatusOption[] = [
    { value: '', label: '全部状态' },
    { value: '1', label: '正常' },
    { value: '0', label: '禁用' }
] 