import axios from '@/axios/axios';
import {ApiResponse} from './types';

/**
 * 删除单条历史记录
 * @param questionId 问题ID
 * @returns 是否成功
 */
export async function deleteHistoryRecord(questionId: string): Promise<boolean> {
    try {
        const response = await axios.delete<ApiResponse<boolean>>(`/service-question/user/history/record/${questionId}`);
        return response.data.status === 200;
    } catch (error) {
        console.error('删除历史记录失败:', error);
        return false;
    }
}

/**
 * 清空指定日期的历史记录
 * @param date 日期字符串
 * @returns 是否成功
 */
export async function clearDayHistoryRecords(date: string): Promise<boolean> {
    try {
        const response = await axios.delete<ApiResponse<boolean>>('/service-question/user/history/day', {
            params: {date}
        });
        return response.data.status === 200;
    } catch (error) {
        console.error('清空历史记录失败:', error);
        return false;
    }
}
