import axios from "@/axios/axios";
import { UserProfileData } from "./ApiUpdateUserProfile";

/**
 * 获取用户个人资料
 * @returns 包含用户资料的响应
 */
export const getUserProfile = async () => {
    const response = await axios.get<{
        status: number;
        message: string;
        data: UserProfileData;
    }>(
        "service-user/user/info/UserProfile"
    );

    return response.data;
}