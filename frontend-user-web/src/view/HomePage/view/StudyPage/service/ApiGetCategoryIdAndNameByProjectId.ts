import axios from "@/axios/axios";

export const apiGetCategoryIdAndNameByProjectId = async () => {
    const response = await axios.get(
        "service-question/user/category/categoryIdAndName"
    )
    return response.data
}