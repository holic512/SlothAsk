import axios from "@/axios/axios"
import {IPasswordForm, IUserForm} from "@/view/MainView/user/list/types/form";

export const apiUpdateUser = async (params: IUserForm) => {
    const response = await axios.put(
        'service-user/admin/updateUser',
        {
            id: params.id,
            username: params.username,
            email: params.email,
            phone: params.phone,
            gender: params.gender,
            bio: params.bio,
            avatar: params.avatar,
            status: params.status,
            nickname: params.nickname,
            age: params.age,
        }
    )
    return response.data
}