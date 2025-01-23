import axios from "@/axios/axios"
import type { IUserListResponse, IUserSearchParams } from "../types/user"

export const apiFetchUserList = async (params: IUserSearchParams): Promise<IUserListResponse> => {
    const response = await axios.get<IUserListResponse>(
        'service-user/admin/list',
        {
            params: {
                keyword: params.keyword,
                status: params.status,
                pageNum: params.pageNum,
                pageSize: params.pageSize
            }
        }
    )
    return response.data
} 