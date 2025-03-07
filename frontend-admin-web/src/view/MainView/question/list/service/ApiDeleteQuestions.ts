import axios from "@/axios/axios";

export const apiDeleteQuestions = async (questionIds: number[]) => {
    const response = await axios.delete(
        "service-question/admin/question/batch",
        {
            data: questionIds,
        }
    );
}