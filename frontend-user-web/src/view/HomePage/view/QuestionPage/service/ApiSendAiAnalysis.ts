import axios from "@/axios/axios";

export interface SendAiAnalysisRequest {
    answerId: number;
}

export const ApiSendAiAnalysis = async (request: SendAiAnalysisRequest) => {
    try {
        const response = await axios.post(
            'service-question/user/aiAnalysis/sendAiAnalysis',
            request
        );
        return response.data;
    } catch (error) {
        console.error('发送AI解析请求失败:', error);
        throw error;
    }
};