import {apiAddUser} from "@/view/MainView/user/list/service/ApiAddUser";
import {IUserForm} from "@/view/MainView/user/list/types/form";
import {ElMessage} from "element-plus";
import {useUserListStore} from "@/view/MainView/user/list/pinia/userListStore";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";

export const handleAddUser = async (userForm: IUserForm) => {
    // 调用api
    const response = await apiAddUser(userForm)

    if (response.status === 200) {
        ElMessage.success("新建用户成功")

        // 更新表单
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

        // 关闭 ui
        userListStore.formVisible = false

    } else {
        ElMessage.warning(response.message)
    }
}