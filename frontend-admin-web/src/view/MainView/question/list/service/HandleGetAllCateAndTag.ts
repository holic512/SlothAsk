import {useQuestionStore} from "@/view/MainView/question/list/pinia/questionStore";
import {apiGetAllCateAndTag} from "@/view/MainView/question/list/service/ApiGetAllCateAndTag";

// 用于获取全部的 分类和标签
export const handleGetAllCateAndTag = async () => {
    const result = await apiGetAllCateAndTag()
    if (result.status === 200) {
        // 问题页面的 store
        const questionStore = useQuestionStore()
        questionStore.categoryOptions = result.data.cateDtoList;
        questionStore.tagOptions = result.data.cateDtoList;
    }


}