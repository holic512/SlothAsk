import axios from "@/axios/axios";
import {QuestionFilter} from "@/view/HomePage/view/StudyPage/store/QuestionBank";

export const apiGetQuestionList = async (pagination: QuestionFilter) => {
    const response = await axios.get(
        "service-question/user/category/questionList",
        {
            params: {
                searchText: pagination.searchText,
                filterCategory: pagination.filterCategory,
                filterType: pagination.filterType,
                filterDifficulty: pagination.filterDifficulty,
                pageNum: pagination.pageNum,
                filterTags: pagination.filterTags
            },
            paramsSerializer: params => {
                const queryParams = new URLSearchParams();
                
                for (const key in params) {
                    if (params[key] === null || params[key] === undefined) continue;
                    
                    if (Array.isArray(params[key])) {
                        params[key].forEach((value: any) => {
                            queryParams.append(key, value);
                        });
                    } else {
                        queryParams.append(key, params[key]);
                    }
                }
                
                return queryParams.toString();
            }
        }
    )

    return response.data;
}