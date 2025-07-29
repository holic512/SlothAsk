/**
 * File Name: ApiMessagePage.ts
 * Description: 消息页面API服务
 * Author: holic512
 * Created Date: 2025-01-20
 * Usage: 提供消息页面相关的API调用
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
 * 分页查询结果接口
 */
interface PageResult<T> {
    records: T[];
    total: number;
    size: number;
    current: number;
    pages: number;
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

// 消息查询过滤条件接口
export interface MessageQueryFilters {
  pageNum?: number;
  pageSize?: number;
  type?: number; // 消息类型过滤
  readStatus?: number; // 阅读状态过滤：0-未读，1-已读
  createTimeStart?: string; // 创建时间范围 - 开始时间
  createTimeEnd?: string; // 创建时间范围 - 结束时间
  keyword?: string; // 关键词搜索
  orderBy?: string; // 排序字段
  orderDirection?: string; // 排序方向：asc-升序，desc-降序
}

/**
 * 分页查询用户消息（支持多种过滤条件）
 * @param filters 查询过滤条件
 * @returns Promise 返回包含分页消息列表的响应
 */
export const getMessagesByPage = async (filters: MessageQueryFilters = {}): Promise<ApiResponse<PageResult<BaseMessage>>> => {
    // 设置默认值
    const queryParams = {
        pageNum: 1,
        pageSize: 10,
        orderBy: "created_time",
        orderDirection: "desc",
        ...filters
    };
    
    const response = await axios.post(
        "service-user/user/basemessage/page",
        queryParams
    );
    
    // 转换后端数据格式
    if (response.data.status === 200 && response.data.data) {
        const pageData = response.data.data;
        const transformedRecords = pageData.records.map((item: BackendMessageDto) => 
            transformBackendMessage(item)
        );
        
        return {
            ...response.data,
            data: {
                records: transformedRecords,
                total: pageData.total,
                size: pageData.pageSize,
                current: pageData.pageNum,
                pages: pageData.totalPages
            }
        };
    }
    
    return response.data;
};

/**
 * 检查用户是否有未读消息
 * @returns Promise 返回检查结果的响应
 */
export const checkHasUnreadMessages = async (): Promise<ApiResponse<boolean>> => {
    const response = await axios.get(
        "service-user/user/basemessage/check-unread-latest-ten"
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

// 注意：后端暂未提供单条消息标记已读和删除消息的接口
// 如需要这些功能，需要后端先实现相应的API接口