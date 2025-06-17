/**
 * File Name: ApiSubmitAnswer.ts
 * Description: 提交答案API接口
 * Author: holic512
 * Created Date: 2025-06-17
 * Version: 1.0
 * Usage:
 * 用于调用后端提交答案接口，将用户答案状态更新为已提交。
 */

import axios from "@/axios/axios";

export interface SubmitAnswerRequest {
    answerId: number;
}

export const ApiSubmitAnswer = async (data: SubmitAnswerRequest) => {
    try {
        const response = await axios.post(
            `service-question/user/answer/submitAnswer`,
            data
        );
        return response.data;
    } catch (error) {
        console.error('提交答案失败:', error);
        throw error;
    }
};