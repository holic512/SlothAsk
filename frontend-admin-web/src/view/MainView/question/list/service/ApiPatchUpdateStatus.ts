import axios from "@/axios/axios";

export const ApiPatchUpdateStatus = async (questionId: number) => {
    const response = await axios.patch(
        "service-question/admin/question/status",
        {},
        {
            params: {
                questionId: questionId,
            }
        }
    )
    return response.data;
}