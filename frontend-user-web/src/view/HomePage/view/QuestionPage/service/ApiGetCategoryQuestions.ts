import axios from "@/axios/axios";

export const ApiGetCategoryQuestions = async (virtualId: string, pageSize: number = 20) => {
    try {
        const response = await axios.get(
            `service-question/user/question/category-questions/${virtualId}`,
            {
                params: {
                    pageSize
                }
            }
        );

        return response.data;
    } catch (error) {
        console.error('API调用失败:', error);
        throw error;
    }
}; 