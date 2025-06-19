import axios from "@/axios/axios";

export interface GetAiAnalysisRequest {
    answerId: number;
}

export interface AiAnalysisRecord {
    id: number;
    answerId: number;
    accuracy: number;
    analysis: string;
    createTime: string;
    updateTime: string;
}

export const ApiGetAiAnalysis = async (request: GetAiAnalysisRequest) => {
    try {
        const response = await axios.post(
            'service-question/user/aiAnalysis/getAiAnalysis',
            request
        );
        return response.data;
    } catch (error) {
        console.error('获取AI解析记录失败:', error);
        throw error;
    }
};

// 检查AI解析状态的接口（用于轮询）
export const ApiCheckAiAnalysisStatus = async (answerId: number) => {
    try {
        const response = await ApiGetAiAnalysis({ answerId });
        return response;
    } catch (error) {
        console.error('检查AI解析状态失败:', error);
        throw error;
    }
};