import axios from "@/axios/axios";
import {QuestionFilter} from "@/view/HomePage/view/StudyPage/store/QuestionBank";

export const apiGetQuestionList = async (pagination: QuestionFilter) => {
    const response = await axios.get(
        "service-question/user/study/questionList",
        {
            params: {
                searchText: pagination.searchText,
                matchAllConditions: pagination.matchAllConditions,
                filterCategoryEquals: pagination.filterCategoryEquals,
                filterCategoryNotEquals: pagination.filterCategoryNotEquals,
                filterTypeEquals: pagination.filterTypeEquals,
                filterTypeNotEquals: pagination.filterTypeNotEquals,
                filterDifficultyEquals: pagination.filterDifficultyEquals,
                filterDifficultyNotEquals: pagination.filterDifficultyNotEquals,
                pageNum: pagination.pageNum,
                filterTags: pagination.filterTags
            },
            paramsSerializer: params => {
                const queryParams = new URLSearchParams();
                
                for (const key in params) {
                    if (params[key] === null || params[key] === undefined) continue;
                    
                    if (Array.isArray(params[key])) {
                        // 只有当数组不为空时才添加参数
                        if (params[key].length > 0) {
                            params[key].forEach((value: any) => {
                                queryParams.append(key, value);
                            });
                        }
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