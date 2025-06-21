import axios from "@/axios/axios";

export const apiPostSignIn = async () => {
    const response = await axios.post(
        "service-user/user/signin/signIn"
    )
    return response.data
}