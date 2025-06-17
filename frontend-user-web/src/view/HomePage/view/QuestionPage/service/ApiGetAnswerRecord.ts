import axios from "@/axios/axios";

export interface AnswerRecordResponse {
    id: number;
    answer: string;
    isSubmitted: boolean;
    updateTime: string;
}

export const ApiGetAnswerRecord = async (virtualId: string) => {
    try {
        const response = await axios.get(
            `service-question/user/answer/getAnswerRecord`,
            {
                params: {
                    virtualId: virtualId
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error('获取答题记录失败:', error);
        throw error;
    }
};