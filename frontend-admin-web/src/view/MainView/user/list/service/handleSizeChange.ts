import { useUserListStore } from "../pinia/userListStore"
import { apiFetchUserList } from "./ApiFetchUserList"

/**
 * 处理每页显示数量变化
 * 当用户修改每页显示数量时重新获取数据
 * @param size - 新的每页显示数量
 */
export const handleSizeChange = async (size: number): Promise<void> => {
    const userListStore = useUserListStore()
    
    // 更新每页显示数量并重置页码
    userListStore.pageSize = size
    userListStore.pageNum = 1
    
    // 获取新的数据
    const response = await apiFetchUserList({
        keyword: userListStore.searchKeyword,
        status: userListStore.searchStatus,
        pageNum: userListStore.pageNum,
        pageSize: userListStore.pageSize
    })
    
    // 更新表格数据和总数
    if (response.status === 200) {
        userListStore.tableData = response.data.list
        userListStore.total = response.data.total
    }
} 