import { useUserListStore } from "../pinia/userListStore"
import type { IUserForm, IPasswordForm } from "../types/form"
import { ElMessage } from 'element-plus'

/**
 * 处理用户表单提交
 * @param formData - 用户表单数据
 */
export const handleUserFormSubmit = async (formData: IUserForm): Promise<void> => {
    const userListStore = useUserListStore()
    try {
        // TODO: 调用API保存数据
        // const response = await apiSaveUser(formData)
        
        ElMessage.success('保存成功')
        userListStore.formVisible = false
        
        // 刷新列表数据
        // TODO: 调用刷新列表的方法
    } catch (error) {
        ElMessage.error('保存失败')
    }
}

/**
 * 处理密码修改提交
 * @param formData - 密码表单数据
 */
export const handlePasswordFormSubmit = async (formData: IPasswordForm): Promise<void> => {
    const userListStore = useUserListStore()
    try {
        // TODO: 调用API修改密码
        // const response = await apiUpdatePassword(formData)
        
        ElMessage.success('密码修改成功')
        userListStore.passwordFormVisible = false
    } catch (error) {
        ElMessage.error('密码修改失败')
    }
} 