/**
 * File Name: ApiRetryAnswer.ts
 * Description: 重新回答API接口
 * Author: holic512
 * Created Date: 2025-06-18
 * Version: 1.0
 * Usage:
 * 用于调用后端重新回答接口，重置用户答案状态为未提交并清除AI解析记录。
 */

import axios from "@/axios/axios";

export interface RetryAnswerRequest {
    answerId: number;
}

export const ApiRetryAnswer = async (data: RetryAnswerRequest) => {
    try {
        const response = await axios.post(
            `service-question/user/aiAnalysis/retryAnswer`,
            data
        );
        return response.data;
    } catch (error) {
        console.error('重新回答失败:', error);
        throw error;
    }
};