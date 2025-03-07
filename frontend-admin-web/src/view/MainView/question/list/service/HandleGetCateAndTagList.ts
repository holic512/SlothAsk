import {ApiGetProjectList} from "@/view/MainView/question/list/service/ApiGetProjectList";
import {useQuestionStore} from "@/view/MainView/question/list/pinia/questionStore";
import {ApiGetCateAndTagList} from "@/view/MainView/question/list/service/ApiGetCateAndTagList";

export const handleGetCateAndTagList = async (ProjectId: number) => {
    const result = await ApiGetCateAndTagList(ProjectId)
    if (result.status === 200) {
        // 问题页面的 store
        const questionStore = useQuestionStore()
        questionStore.categoryOptionsByProjectId = result.data.cateDtoList;
        questionStore.tagOptionsByProjectId = result.data.tagDtoList;
    }
}