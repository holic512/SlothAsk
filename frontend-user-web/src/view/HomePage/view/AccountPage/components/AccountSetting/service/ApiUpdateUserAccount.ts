import axios from "@/axios/axios";

/**
 * 更新用户名请求参数
 */
export interface UpdateUsernameParams {
    username: string;
}

/**
 * 更新邮箱请求参数
 */
export interface UpdateEmailParams {
    email: string;
}

export interface InitPasswordParams {
    password: string;
}

/**
 * 更新密码请求参数
 */
export interface UpdatePasswordParams {
    oldPassword: string;
    newPassword: string;
}

/**
 * 密码重置验证参数
 */
export interface ResetPasswordParams {
    key: string;
    code: string;
    newPassword: string;
}

/**
 * 双重验证更新邮箱参数
 */
export interface UpdateEmailWithTwoStepsParams {
    originalUid: string;
    originalCode: string;
    newEmail: string;
    newUid: string;
    newCode: string;
}

/**
 * 密码验证更新邮箱参数
 */
export interface UpdateEmailWithPasswordParams {
    uid: string;
    email: string;
    code: string;
    password: string;
}

/**
 * GitHub绑定验证码验证参数
 */
export interface VerifyGithubBindCodeParams {
    uid: string;
    code: string;
}

/**
 * 更新用户名
 * 注意：此API接口需要后端实现，目前后端尚未提供此接口
 * 预期接口路径：service-user/user/account/username
 * 请求方式：PUT
 *
 * @param params 包含新用户名的参数对象
 * @returns 更新结果响应
 */
export const updateUsername = async (params: UpdateUsernameParams) => {

    const response = await axios.put(
        "service-user/user/account/username",
        params
    );
    // 成功响应
    return response.data;
}

export const initPassword = async (params: InitPasswordParams) => {
    const response = await axios.put(
        "service-user/user/account/initPassword",
        params
    );
    // 成功响应
    return response.data;
}

/**
 * 更新用户密码（通过旧密码验证）
 * 接口路径：service-user/user/account/password
 * 请求方式：PUT
 *
 * @param params 包含旧密码和新密码的参数对象
 * @returns 更新结果响应
 */
export const updatePassword = async (params: UpdatePasswordParams) => {
    const response = await axios.put(
        "service-user/user/account/password",
        params
    );
    // 成功响应
    return response.data;
}

/**
 * 更新用户邮箱
 * 注意：此API接口需要后端实现，目前后端尚未提供此接口
 * 预期接口路径：service-user/user/account/email
 * 请求方式：PUT
 *
 * @param params 包含新邮箱的参数对象
 * @returns 更新结果响应
 */
export const updateEmail = async (params: UpdateEmailParams) => {
    // TODO: 后端接口尚未实现，此处为前端预留代码
    // 当前为模拟实现，实际项目中需要等待后端接口开发完成
    // const response = await axios.put<{
    //     status: number;
    //     message: string;
    //     data: boolean;
    // }>(
    //     "service-user/user/account/email",
    //     params
    // );

    // 模拟成功响应
    return {
        status: 200,
        message: "邮箱更新成功",
        data: true
    };
}

/**
 * 发送密码重置验证码
 * 接口路径：service-user/user/account/sendPasswordResetCode
 * 请求方式：POST
 *
 * @returns 包含验证Key和脱敏邮箱的响应
 */
export const sendPasswordResetCode = async () => {
    const response = await axios.post(
        "service-user/user/account/sendPasswordResetCode"
    );
    // 返回响应数据
    return response.data;
}

/**
 * 通过验证码重置密码
 * 接口路径：service-user/user/account/resetPassword
 * 请求方式：PUT
 *
 * @param params 包含验证key、验证码和新密码的参数对象
 * @returns 重置结果响应
 */
export const resetPasswordByCode = async (params: ResetPasswordParams) => {
    const response = await axios.put(
        "service-user/user/account/resetPassword",
        params
    );
    // 成功响应
    return response.data;
}

/**
 * 向原邮箱发送验证码
 * 接口路径：service-user/user/account/sendVerificationToOriginalEmail
 * 请求方式：POST
 *
 * @returns 包含验证信息的响应
 */
export const sendVerificationToOriginalEmail = async () => {
    const response = await axios.post(
        "service-user/user/account/sendVerificationToOriginalEmail"
    );
    return response.data;
}

/**
 * 向新邮箱发送验证码
 * 接口路径：service-user/user/account/sendEmailChangeCode
 * 请求方式：POST
 *
 * @param email 新邮箱地址
 * @returns 包含验证信息的响应
 */
export const sendEmailChangeCode = async (email: string) => {
    const response = await axios.post(
        `service-user/user/account/sendEmailChangeCode?email=${encodeURIComponent(email)}`
    );
    return response.data;
}

/**
 * 双重验证更新邮箱
 * 接口路径：service-user/user/account/updateEmailWithTwoSteps
 * 请求方式：PUT
 *
 * @param params 包含原邮箱和新邮箱验证信息的参数
 * @returns 更新结果响应
 */
export const updateEmailWithTwoSteps = async (params: UpdateEmailWithTwoStepsParams) => {
    const response = await axios.put(
        "service-user/user/account/updateEmailWithTwoSteps",
        params
    );
    return response.data;
}

/**
 * 通过密码验证更新邮箱
 * 接口路径：service-user/user/account/updateEmailWithPassword
 * 请求方式：PUT
 *
 * @param params 包含密码和验证信息的参数
 * @returns 更新结果响应
 */
export const updateEmailWithPassword = async (params: UpdateEmailWithPasswordParams) => {
    const response = await axios.put(
        "service-user/user/account/updateEmailWithPassword",
        params
    );
    return response.data;
}

/**
 * 发送GitHub绑定验证码
 * 接口路径：service-user/user/account/sendGithubBindCode
 * 请求方式：POST
 *
 * @returns 包含uid和脱敏邮箱的响应
 */
export const sendGithubBindCode = async () => {
    const response = await axios.post(
        "service-user/user/account/sendGithubBindCode"
    );
    return response.data;
}

/**
 * 验证GitHub绑定验证码
 * 接口路径：service-user/user/account/verifyGithubBindCode
 * 请求方式：POST
 *
 * @param params 包含uid和验证码的参数
 * @returns 验证结果响应
 */
export const verifyGithubBindCode = async (params: VerifyGithubBindCodeParams) => {
    const response = await axios.post(
        "service-user/user/account/verifyGithubBindCode",
        params
    );
    return response.data;
}

/**
 * 解绑GitHub账号
 * 接口路径：service-user/user/account/unbindGithub
 * 请求方式：DELETE
 *
 * @returns 解绑结果响应
 */
export const unbindGithub = async () => {
    const response = await axios.delete(
        "service-user/user/account/unbindGithub"
    );
    return response.data;
}