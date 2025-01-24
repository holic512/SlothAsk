import axios from "@/axios/axios"
import {IPasswordForm} from "@/view/MainView/user/list/types/form";

export const apiUpdatePassword = async (params: IPasswordForm) => {
    const response = await axios.put(
        'service-user/admin/UpdatePassword',
        {
            id: params.id,
            newPassword:params.password,
        }
    )
    return response.data
}