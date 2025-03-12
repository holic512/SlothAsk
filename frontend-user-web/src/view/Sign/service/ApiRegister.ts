/**
 * File Name: ApiRegister.ts
 * Description: 用户注册API服务
 * Author: holic512
 * Created Date: 2025-03-11
 * Usage: 提供用户注册相关的API调用
 */

import axios from "@/axios/axios";

interface RegisterParams {
    email: string;
    username: string;
    password: string;
    uid: string;
}

/**
 * 用户注册API
 * @param params 注册参数，包含邮箱、用户名、密码和uid
 * @returns Promise 返回注册结果
 */
export const apiRegister = async (params: RegisterParams) => {
    const response = await axios.post(
        "service-user/user/sign/register",
        params
    );
    return response.data;
}; 