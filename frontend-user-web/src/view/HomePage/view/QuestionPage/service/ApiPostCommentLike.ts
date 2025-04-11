import request from "@/axios/request";

export function ApiPostCommentLike(commentId: number) {
    return request.post("service-question/user/question/comment/like", null, {
        params: {commentId}
    });
}