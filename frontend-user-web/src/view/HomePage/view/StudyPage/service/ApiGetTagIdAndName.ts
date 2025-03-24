import axios from "@/axios/axios";

export const apiGetTagIdAndName = async () => {
    const response = await axios.get(
        "service-question/user/category/tagIdAndName"
    )
    return response.data
}