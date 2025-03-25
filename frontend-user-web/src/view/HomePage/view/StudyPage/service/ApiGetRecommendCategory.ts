import axios from "@/axios/axios";

export const ApiGetRecommendCategory = async () => {
    const response = await axios.get(
        "service-question/user/study/recommend"
    )
    return response.data
}