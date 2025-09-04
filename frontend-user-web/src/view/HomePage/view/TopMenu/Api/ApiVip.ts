/**
 * File Name: ApiVip.ts
 * Description: VIP信息API服务
 * Author: holic512
 * Created Date: 2025-01-20
 * Usage: 提供获取用户VIP信息的API调用
 */

import axios from "@/axios/axios";

/**
 * VIP信息响应接口
 */
interface VipInfoResponse {
    status: number;
    message: string;
    data: {
        id: number;
        userId: number;
        vipType: number;
        startTime: string;
        endTime: string;
        isActive: boolean;
    }
}

/**
 * VIP状态检查响应接口
 */
interface VipStatusResponse {
    status: number;
    message: string;
    data: boolean;
}

/**
 * 获取用户VIP信息
 * @returns Promise 返回包含用户VIP详细信息的对象
 */
export const getUserVipInfo = async (): Promise<VipInfoResponse> => {
    const response = await axios.get(
        "service-vip/vip/user"
    );
    return response.data;
};

/**
 * 检查用户VIP是否生效
 * @returns Promise 返回VIP是否生效的布尔值
 */
export const checkVipStatus = async (): Promise<VipStatusResponse> => {
    const response = await axios.get(
        "service-vip/vip/check"
    );
    return response.data;
};