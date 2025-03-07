import axios from "@/axios/axios";

export const apiAddQuestion = async (Form: any) => {
    const response = await axios.post(
        "service-question/admin/question/addQuestion",
        Form
    )
    return response.data;
}