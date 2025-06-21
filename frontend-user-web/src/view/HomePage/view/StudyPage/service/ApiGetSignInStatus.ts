import axios from "@/axios/axios";

export const apiGetSignInStatus = async () => {
    const response = await axios.get(
        "service-user/user/signin/status"
    )
    return response.data
}