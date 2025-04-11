import axios from "@/axios/axios";

export const ApiGetQuestionCommentByVirtualId = async (virtualId: string) => {
    const response = await axios.get(
        `service-question/user/question/comment/${virtualId}`
    )
    return response.data;
}