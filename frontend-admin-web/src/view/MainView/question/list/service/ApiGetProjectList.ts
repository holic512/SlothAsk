import axios from "@/axios/axios";


export const ApiGetProjectList = async () => {
    const response = await axios.get(
        "service-question/admin/question/project"
    )
    return response.data;
}