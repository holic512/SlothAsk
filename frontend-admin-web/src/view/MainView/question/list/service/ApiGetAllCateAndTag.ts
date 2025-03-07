import axios from "@/axios/axios";

export const apiGetAllCateAndTag = async () => {
    const response = await axios.get(
        "service-question/admin/question/AllCateAndTag",
    )
    return response.data;
}