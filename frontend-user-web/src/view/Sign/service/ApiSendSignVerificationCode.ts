import axios from "@/axios/axios";

export const apiSendSignVerificationCode = async (mail: string) => {
    const response = await axios.post(
        "service-user/user/sign/sendSignVerificationCode",
        {},
        {
            params: {
                mail: mail
            }
        }
    )
    return response.data;
}