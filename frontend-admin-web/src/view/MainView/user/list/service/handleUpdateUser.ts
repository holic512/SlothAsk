import {IUserForm} from "@/view/MainView/user/list/types/form";
import {apiUpdateUser} from "@/view/MainView/user/list/service/ApiUpdateUser";
import {ElMessage} from "element-plus";
import {useUserListStore} from "@/view/MainView/user/list/pinia/userListStore";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";

export const handleUpdateUser = async (userForm: IUserForm) => {
    console.log(userForm)

    const response = await apiUpdateUser(userForm)

    if (response.status === 200) {
        ElMessage.success("更新用户信息成功")

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