import {IUserForm} from "@/view/MainView/user/list/types/form";
import {apiUpdateUser} from "@/view/MainView/user/list/service/ApiUpdateUser";
import {ElMessage} from "element-plus";
import {useUserListStore} from "@/view/MainView/user/list/pinia/userListStore";
import {apiFetchUserList} from "@/view/MainView/user/list/service/ApiFetchUserList";
import {handleFetchDefaultUserList} from "@/view/MainView/user/list/service/handleFetchDefaultUserList";

export const handleUpdateUser = async (userForm: IUserForm) => {

    const response = await apiUpdateUser(userForm)

    if (response.status === 200) {
        ElMessage.success("更新用户信息成功")

        // 调用初始化用户数据列表
        await handleFetchDefaultUserList()
        
        // 关闭 ui
        const userListStore = useUserListStore()
        userListStore.formVisible = false

    } else {
        ElMessage.warning(response.message)
    }

}