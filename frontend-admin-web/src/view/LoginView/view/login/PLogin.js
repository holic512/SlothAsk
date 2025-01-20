import axios from "../../../../axios/axios.js";

export const PLogin = async (username, password, captchaAnswer, captchaId) => {
    const response = await axios.post(
        "service-admin/auth/login", {
            "username": username,
            "password": password,
            "captchaId": captchaId,
            "captchaAnswer": captchaAnswer,
        })
    return response.data;
}