import {apiAddUser} from "@/view/MainView/user/list/service/ApiAddUser";
import {IUserForm} from "@/view/MainView/user/list/types/form";
import {ElMessage} from "element-plus";
import {useUserListStore} from "@/view/MainView/user/list/pinia/userListStore";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";
import {handleFetchDefaultUserList} from "@/view/MainView/user/list/service/handleFetchDefaultUserList";

export const handleAddUser = async (userForm: IUserForm) => {
    // 调用api
    const response = await apiAddUser(userForm)

    if (response.status === 200) {
        ElMessage.success("新建用户成功")

        // 调用初始化用户数据列表
        await handleFetchDefaultUserList()

        // 关闭 ui
        const userListStore = useUserListStore()
        userListStore.formVisible = false

    } else {
        ElMessage.warning(response.message)
    }
}