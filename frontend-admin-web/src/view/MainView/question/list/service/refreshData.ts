// 刷新数据
import type {IQuestionSearchParams} from "@/view/MainView/question/list/types/types";
import {apiGetQuestionList} from "@/view/MainView/question/list/service/ApiGetQuestionList";
import {useQuestionStore} from "@/view/MainView/question/list/pinia/questionStore";

export const refreshData = async () => {
    const  questionStore = useQuestionStore();
    // 重新请求数据
    const search: IQuestionSearchParams = {
        pageNum: questionStore.pageNum,
        pageSize: questionStore.pageSize
    };
    const response = await apiGetQuestionList(search);

    // 更新 table 数据
    questionStore.tableData = response.data.records;
    questionStore.total = response.data.total;
}