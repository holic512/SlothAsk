import axios from "@/axios/axios";


export const ApiGetCateAndTagList = async (ProjectId:number) => {
    const response = await axios.get(
        "service-question/admin/question/CateAndTag",
        {
            params: {
                ProjectId: ProjectId
            }
        }
    )
    return response.data;
}