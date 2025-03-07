import axios from "@/axios/axios";

export const apiGetContentByQuestionId = async (questionId: number) => {
    const response = await axios.get(
        "service-question/admin/question/content",
        {
            params: {
                QuestionId: questionId,
            }
        }
    )
    return response.data;
}