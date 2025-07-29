/**
 * File Name: ApiBaseMessage.ts
 * Description: 基础消息API服务
 * Author: holic512
 * Created Date: 2025-01-20
 * Usage: 提供基础消息相关的API调用
 */

import axios from "@/axios/axios";
import type { BaseMessage } from "@/view/HomePage/components/Message/MessageInterface";

/**
 * API响应接口
 */
interface ApiResponse<T = any> {
    status: number;
    message: string;
    data: T;
}

/**
 * 后端返回的消息数据结构
 */
interface BackendMessageDto {
    id: number;
    type: number;
    readStatus: number;
    messageData: string;
    createTime: string;
}

/**
 * 将后端消息数据转换为前端BaseMessage格式
 */
const transformBackendMessage = (backendMessage: BackendMessageDto): BaseMessage => {
    return {
        messageId: backendMessage.id.toString(), // 将id转换为messageId
        type: backendMessage.type,
        readStatus: backendMessage.readStatus,
        createTime: backendMessage.createTime,
        messageData: JSON.parse(backendMessage.messageData) // 解析JSON字符串
    };
};

/**
 * 获取用户最新创建的前十条消息
 * @returns Promise 返回包含前十条消息列表的响应
 */
export const getLatestTenMessages = async (): Promise<ApiResponse<BaseMessage[]>> => {
    const response = await axios.get(
        "service-user/user/basemessage/latest-ten"
    );
    
    // 转换后端数据格式
    if (response.data.status === 200 && response.data.data) {
        const transformedData = response.data.data.map((item: BackendMessageDto) => 
            transformBackendMessage(item)
        );
        return {
            ...response.data,
            data: transformedData
        };
    }
    
    return response.data;
};

/**
 * 检查用户最新创建的前十条消息中是否存在未读消息
 * @returns Promise 返回检查结果的响应
 */
export const checkUnreadMessagesInLatestTen = async (): Promise<ApiResponse<boolean>> => {
    const response = await axios.get(
        "service-user/user/basemessage/check-unread-latest-ten"
    );
    return response.data;
};

/**
 * 批量已读用户最新创建的前十条消息
 * @returns Promise 返回更新数量的响应
 */
export const markLatestTenMessagesAsRead = async (): Promise<ApiResponse<number>> => {
    const response = await axios.put(
        "service-user/user/basemessage/mark-latest-ten-read"
    );
    return response.data;
};

/**
 * 批量已读用户的全部未读消息
 * @returns Promise 返回更新数量的响应
 */
export const markAllMessagesAsRead = async (): Promise<ApiResponse<number>> => {
    const response = await axios.put(
        "service-user/user/basemessage/mark-all-read"
    );
    return response.data;
};