import axios from "@/axios/axios";
import {IQuestion} from "@/view/MainView/question/list/types/types";

export const ApiPatchUpdateStatus = async (question: IQuestion) => {
    const response = await axios.patch(
        "service-question/admin/question/question",
        question
    )
    return response.data;
}