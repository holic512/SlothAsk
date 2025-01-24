// 查看用户详情
import { useUserListStore } from "../pinia/userListStore"
import type { IUser } from "../types/user"

/**
 * 处理查看用户详情
 * 打开用户详情抽屉并显示用户信息
 * @param row - 要查看的用户数据
 */
export const handleProfileView = (row: IUser): void => {
    // 获取 store 实例
    const userListStore = useUserListStore()

    // 设置当前查看的用户并显示抽屉
    userListStore.currentUser = row
    userListStore.drawerVisible = true
}