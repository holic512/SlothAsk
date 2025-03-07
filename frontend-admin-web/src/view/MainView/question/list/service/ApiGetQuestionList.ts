import type {IQuestionSearchParams} from "@/view/MainView/question/list/types/types";
import axios from "@/axios/axios";

export const apiGetQuestionList =  async (searchForm:IQuestionSearchParams) => {
    const response = await axios.get(
        'service-question/admin/question/search',
        {
            params: searchForm
        }
    )
    return response.data;
}