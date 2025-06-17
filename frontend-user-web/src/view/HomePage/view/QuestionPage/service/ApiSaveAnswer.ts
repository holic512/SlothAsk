import axios from "@/axios/axios";

export interface SaveAnswerRequest {
    virtualId: string;
    answer: string;
}

export const ApiSaveAnswer = async (data: SaveAnswerRequest) => {
    try {
        const response = await axios.post(
            `service-question/user/answer/saveAnswer`,
            data
        );
        return response.data;
    } catch (error) {
        console.error('保存答案失败:', error);
        throw error;
    }
};