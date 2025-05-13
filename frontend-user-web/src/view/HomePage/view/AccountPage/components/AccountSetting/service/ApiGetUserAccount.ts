import axios from "@/axios/axios";

/**
 * 用户账户信息数据接口
 */
export interface UserAccountData {
    username: string;
    email: string;
    remainingUsernameChanges: number;
}

/**
 * 获取用户账户信息（用户名和邮箱）
 * @returns 包含用户账户信息的响应
 */
export const getUserAccount = async () => {
    const response = await axios.get(
        "service-user/user/account/UsernameAndEmail"
    );

    return response.data;
}

/**
 * 获取用户密码是否为空(是否设置过-第三方登录注册来的)
 * @returns 包含用户密码是否为空的响应 data 如果为 true为未初始化  false为已初始化
 */
export const checkPasswordEmpty = async () => {
    const response = await axios.get(
        "service-user/user/account/checkPasswordEmpty"
    );

    return response.data;
}

/**
 * 检查用户是否绑定GitHub账号
 * @returns 包含用户是否绑定GitHub的响应 data 如果为 true为已绑定  false为未绑定
 */
export const checkGithubBound = async () => {
    const response = await axios.get(
        "service-user/user/account/isGithubBound"
    );

    return response.data;
}