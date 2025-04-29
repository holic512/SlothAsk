import axios from "@/axios/axios";

export  const getCategoryById= (categoryId:number) => {
    return axios.get(`service-question/user/questionBank/categoryById/${categoryId}`);
};

export const getCategoriesByProjectId = (projectId:number) => {
    return axios.get(`service-question/user/questionBank/categoriesByProjectId/${projectId}`);
};