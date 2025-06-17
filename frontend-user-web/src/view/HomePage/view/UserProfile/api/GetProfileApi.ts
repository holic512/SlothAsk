import axios from "@/axios/axios";

export const getUserProfileApi = async (username: string) => {
    const response = await axios.get(
        "service-user/user/profile/user/profileInfo/" + username
    )
    return response.data
}

export const getUserCommentListApi = async (username: string, page: number = 1, size: number = 10) => {
    const response = await axios.get(
        `service-user/user/profile/user/commentList/${username}?page=${page}&size=${size}`
    )
    return response.data
}

export const getUserFavQuestionListApi = async (username: string, page: number = 1, size: number = 10) => {
    const response = await axios.get(
        `service-user/user/profile/user/favQuestionList/${username}?page=${page}&size=${size}`
    )
    return response.data
}