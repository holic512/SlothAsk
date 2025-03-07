import axios from "@/axios/axios";

export const apiDeleteQuestion = async (questionId: number) => {
    const response = await axios.delete(`service-question/admin/question/${questionId}`);
}