import {ApiGetProjectList} from "@/view/MainView/question/list/service/ApiGetProjectList";
import {useQuestionStore} from "@/view/MainView/question/list/pinia/questionStore";

export const handleGetProjectList = async () => {
    const result = await ApiGetProjectList()
    if (result.status === 200) {
        // 问题页面的 store
        const questionStore = useQuestionStore()
        questionStore.projectOptions = result.data;
    }
}