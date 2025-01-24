import { ElMessageBox, ElMessage } from 'element-plus'
import type { IUser } from "../types/user"
import {apiDeleteUser} from "@/view/MainView/user/list/service/ApiDeleteUser";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";
import {useUserListStore} from "@/view/MainView/user/list/pinia/userListStore";
import {handleFetchDefaultUserList} from "@/view/MainView/user/list/service/handleFetchDefaultUserList";

/**
 * 处理删除单个用户操作
 * @param row - 要删除的用户数据
 */
export const handleDelete = async (row: IUser): Promise<void> => {
    try {
        await ElMessageBox.confirm(
            `确认删除用户 ${row.username} 吗？`,
            '删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        const response = await apiDeleteUser(row.id);
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