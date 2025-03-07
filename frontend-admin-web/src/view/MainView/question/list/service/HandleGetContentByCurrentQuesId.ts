import {useQuestionStore} from "@/view/MainView/question/list/pinia/questionStore";
import {apiGetContentByQuestionId} from "@/view/MainView/question/list/service/ApiGetContentByQuestionId";
// 获取当前 问题 的 题目内容
export const handlerGetContentByCurrentQuesId = async () => {
    const questionStore = useQuestionStore()

    // 获取当前问题对象是否存在
    if (!questionStore.currentQuestion) return;

    // 获取当前 问题的 id
    const id = questionStore.currentQuestion?.id
    if (!id) return
    const result = await apiGetContentByQuestionId(id);
    if (result.status === 200) {
        questionStore.currentQuestion.content = result.data;
    }

}