import axios from "@/axios/axios";

export const ApiGetQuestionAnswerByVirtualId = async (virtualId: string) => {
    const response = await axios.get(
        `service-question/user/question/question/answer/${virtualId}`
    )
    return response.data;
}