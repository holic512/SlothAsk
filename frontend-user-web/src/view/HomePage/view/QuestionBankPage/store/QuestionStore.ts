import {defineStore} from "pinia";
import {ref} from "vue";
import {getQuestionCountByCategoryId,
        getQuestionsByCategoryId} from "@/view/HomePage/view/QuestionBankPage/service/ApiGetQuestion";



export const useQuestionStore = defineStore('question', () => {
    const questionCount = ref<number>(0);
    const questions= ref<Question[]>([]);
    // 获取问题数量
    const fetchQuestionCount = async (categoryId: number) => {
        const response = await getQuestionCountByCategoryId(categoryId);
        questionCount.value = response.data.data;

    };

    // 获取问题列表
    const fetchQuestionsByCategoryId = async (categoryId: number) => {
        const response = await getQuestionsByCategoryId(categoryId);
        questions.value = response.data.data;
    }


    return {
        questions,
        questionCount,
        fetchQuestionsByCategoryId,
        fetchQuestionCount
    }
});