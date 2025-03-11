import axios from "@/axios/axios";

export const ApiVerifySignVerificationCode = async (email: string, code: string) => {
    const response = await axios.post(
        "service-user/user/sign/verifySignVerificationCode",
        {
            email: email,
            code: code,
        }
    )
    return response.data;
}