import { useUserListStore } from "../pinia/userListStore"
import type { IUser } from "../types/user"

/**
 * 处理添加用户操作
 * 打开新增用户表单
 */
export const handleAdd = (): void => {
    const userListStore = useUserListStore()
    userListStore.formType = 'add'
    userListStore.currentEditUser = null
    userListStore.formVisible = true
}

/**
 * 处理编辑用户操作
 * @param row - 当前选中的用户数据
 */
export const handleEdit = (row: IUser): void => {
    const userListStore = useUserListStore()
    userListStore.formType = 'edit'
    userListStore.currentEditUser = row
    userListStore.formVisible = true
} 