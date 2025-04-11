import request from "@/axios/request";

export function ApiPostQuestionComment(data: {
    questionId: string;
    userId: number | string;
    content: string;
    parentId?: number | null;
}) {
    return request.post("service-question/user/question/comment/add", data); // 直接将 data 作为请求体发送
}