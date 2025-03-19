import axios from "@/axios/axios";

export const ApiGetRecommendCategory = async () => {
    const response = await axios.get(
        "service-question/user/category/recommend"
    )
    return response.data
}