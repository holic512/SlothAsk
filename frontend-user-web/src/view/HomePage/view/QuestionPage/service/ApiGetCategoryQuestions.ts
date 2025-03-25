import axios from "@/axios/axios";

export const ApiGetCategoryQuestions = async (virtualId: string, page?: number) => {
    try {
        const response = await axios.get(
            `service-question/user/question/category-questions/${virtualId}`,
            {
                params: {
                    page: page,
                }
            }
        );

        return response.data;
    } catch (error) {
        console.error('API调用失败:', error);
        throw error;
    }
}; 