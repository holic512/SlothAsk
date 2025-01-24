import {useUserListStore} from "../pinia/userListStore"
import type {IUser} from "../types/user"
import type {IPasswordForm} from "../types/form"
import {apiUpdatePassword} from "@/view/MainView/user/list/service/ApiUpdatePassword";
import {ElMessage} from "element-plus";

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

    // 调用api
    const response = await apiUpdatePassword(formData)

    if (response.status === 200) {
        // 修改成功
        ElMessage.success("密码修改成功")
        userListStore.passwordFormVisible = false

    } else {
        ElMessage.warning("密码修改失败")
    }

} 