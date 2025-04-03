import axios from "@/axios/axios";

export const getProject = () => {
    return axios.get(`service-question/user/questionBank/project`);
};