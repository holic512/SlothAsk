import { useUserListStore } from "../pinia/userListStore"
import type { IUser } from "../types/user"

/**
 * 处理表格多选操作
 * @param rows - 当前选中的所有用户数据
 */
export const handleSelectionChange = (rows: IUser[]): void => {
    const userListStore = useUserListStore()
    userListStore.selectedRows = rows
} 