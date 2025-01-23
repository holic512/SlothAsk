import { useUserListStore } from "../pinia/userListStore"
import { apiFetchUserList } from "./ApiFetchUserList"
import type { ISearchParams } from "../types/search"

/**
 * 处理搜索操作
 * 根据搜索条件重新获取用户列表数据
 * @param params - 搜索参数，包含关键字和状态
 */
export const handleSearch = async (params: ISearchParams): Promise<void> => {
    const userListStore = useUserListStore()
    
    // 更新搜索条件
    userListStore.searchKeyword = params.keyword
    userListStore.searchStatus = params.status
    // 重置页码
    userListStore.pageNum = 1
    
    // 获取搜索结果
    const response = await apiFetchUserList({
        keyword: params.keyword,
        status: params.status,
        pageNum: userListStore.pageNum,
        pageSize: userListStore.pageSize
    })
    
    // 更新表格数据和总数
    if (response.status === 200) {
        userListStore.tableData = response.data.list
        userListStore.total = response.data.total
    }
} 