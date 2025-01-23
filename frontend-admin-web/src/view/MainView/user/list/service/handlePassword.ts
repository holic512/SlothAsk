import { useUserListStore } from "../pinia/userListStore"
import type { IUser } from "../types/user"
import type { IPasswordForm } from "../types/form"

/**
 * 处理修改密码操作
 * 打开修改密码表单
 * @param row - 要修改密码的用户数据
 */
export const handlePassword = (row: IUser): void => {
    const userListStore = useUserListStore()
    userListStore.currentPasswordUser = row
    userListStore.passwordFormVisible = true
}

/**
 * 处理密码修改提交
 * @param formData - 包含新密码的表单数据
 */
export const handlePasswordSubmit = async (formData: IPasswordForm): Promise<void> => {
    const userListStore = useUserListStore()
    // TODO: 调用API修改密码
    userListStore.passwordFormVisible = false
} 