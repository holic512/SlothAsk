import {useUserListStore} from "@/view/MainView/user/list/pinia/userListStore";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";

export const handleFetchDefaultUserList = async () => {
    const userListStore = useUserListStore()
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