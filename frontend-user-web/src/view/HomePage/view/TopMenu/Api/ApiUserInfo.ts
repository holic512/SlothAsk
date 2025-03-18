/**
 * File Name: ApiUserInfo.ts
 * Description: 用户信息API服务
 * Author: holic512
 * Created Date: 2025-03-18
 * Usage: 提供获取用户基本信息的API调用
 */

import axios from "@/axios/axios";

/**
 * 用户基本信息接口
 */
interface UserInfoResponse {
    status: number;
    message: string;
    data: {
        nickname: string;
        avatar: string;
    }
}

/**
 * 获取用户昵称和头像信息
 * @returns Promise 返回包含用户昵称和头像的对象
 */
export const getUserNameAndAvatar = async (): Promise<UserInfoResponse> => {
    const response = await axios.get(
        "service-user/user/info/UserNameAndAvtar"
    );
    return response.data;
}; 