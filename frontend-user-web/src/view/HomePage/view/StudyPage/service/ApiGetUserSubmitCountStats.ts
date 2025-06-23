import axios from "@/axios/axios";

export const apiGetUserSubmitCountStats = async () => {
    const response = await axios.get(
        "service-question/user/study/submitCountStats"
    )
    return response.data
}