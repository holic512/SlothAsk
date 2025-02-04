import axios from "@/axios/axios";

export const ApiGetTagsList = async (params: {
    keyword?: string,
    pageNum: number,
    pageSize: number
}) => {
    const response = await axios.get(
        'service-question/admin/tags/list',
        { params }
    )
    return response.data.data
} 