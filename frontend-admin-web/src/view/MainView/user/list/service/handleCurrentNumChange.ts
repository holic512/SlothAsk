import { useUserListStore } from "../pinia/userListStore"
import { apiFetchUserList } from "./ApiFetchUserList"

/**
 * 处理页码变化
 * 当用户切换页码时重新获取对应页的数据
 * @param currentNum - 新的页码
 */
export const handleCurrentNumChange = async (currentNum: number): Promise<void> => {
    const userListStore = useUserListStore()
    
    // 更新当前页码
    userListStore.pageNum = currentNum
    
    // 获取新页码的数据
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