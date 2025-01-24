import axios from "@/axios/axios"
import type {IUserForm} from "../types/form"

export const apiAddUser = async (datas: IUserForm) => {
    const response = await axios.post(
        'service-user/admin/addUser',
        {
            password: datas.password,
            gender: datas.gender,
            phone: datas.phone,
            nickname: datas.nickname,
            bio: datas.bio,
            avatar: datas.avatar,
            email: datas.email,
            age: datas.age,
            username: datas.username,
            status: datas.status,
        }
    )
    return response.data
}