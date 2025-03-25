import axios from "@/axios/axios";

export const ApiGetQuestionByVirtualId = async (virtualId: string) => {
    const response = await axios.get(
        `service-question/user/question/question/${virtualId}`
    )
    return response.data;
}