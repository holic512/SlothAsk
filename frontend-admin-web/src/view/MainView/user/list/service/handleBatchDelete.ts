import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserListStore } from "../pinia/userListStore"
import {apiDeleteBatchUsers} from "@/view/MainView/user/list/service/ApiDeleteBatchUsers";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";
import {handleFetchDefaultUserList} from "@/view/MainView/user/list/service/handleFetchDefaultUserList";

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
        // 获取用户 id 列表
        const selectedUserIds = userListStore.selectedRows.map(user => user.id);

        const response = await apiDeleteBatchUsers(selectedUserIds)

        if (response.status === 200) {
            ElMessage.success('用户删除成功')

            // 调用初始化用户数据列表
            await handleFetchDefaultUserList()

        }else {
            ElMessage.warning('用户删除失败')
        }
    } catch {
        // 用户取消删除
    }
} 