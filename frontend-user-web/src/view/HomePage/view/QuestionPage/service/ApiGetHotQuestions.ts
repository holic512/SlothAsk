import axios from "@/axios/axios";

/**
 * 获取热门题目列表，返回访问量最高的10道题目
 * @returns 热门题目列表的API响应
 */
export const ApiGetHotQuestions = async () => {
    try {
        const response = await axios.get(
            `service-question/user/question/hot-questions`
        );

        return response.data;
    } catch (error) {
        console.error('获取热门题目列表失败:', error);
        throw error;
    }
}; 