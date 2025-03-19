import axios from "@/axios/axios";

/**
 * 用户资料接口类型定义
 */
export interface UserProfileData {
    nickname: string;
    avatar?: string;
    gender: number;
    age?: number | null;
    birthday?: string | null;
    location?: string | null;
    occupation?: number | null;
    bio?: string | null;
}

/**
 * 更新用户个人资料
 * @param userProfile 用户资料对象
 * @returns 请求响应
 */
export const updateUserProfile = async (userProfile: UserProfileData) => {
    const response = await axios.put(
        "service-user/user/info/UserProfile",
        userProfile
    );

    return response.data;
}; 