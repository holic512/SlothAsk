import axios from "@/axios/axios";

export const apiGetAnswerByQuestionId = async (questionId: number) => {
    const response = await axios.get(
        "service-question/admin/question/answer",
        {
            params: {
                QuestionId: questionId,
            }
        }
    )
    return response.data;
}