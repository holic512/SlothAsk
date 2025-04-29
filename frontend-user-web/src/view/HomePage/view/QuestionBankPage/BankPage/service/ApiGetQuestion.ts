import axios from "@/axios/axios";

export const getQuestionCountByCategoryId = (categoryId:number) => {
    return axios.get(`service-question/user/questionBank/count/${categoryId}`)
};

export const getQuestionsByCategoryId = (categoryId:number) => {
    return axios.get(`service-question/user/questionBank/questions/${categoryId}`)
};

