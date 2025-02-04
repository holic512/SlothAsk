import axios from "@/axios/axios";

export const ApiDeleteBatchTags = async (ids: number[]) => {
    const response = await axios.delete(
        'service-question/admin/tags/batchDelete',
        { data: ids }  // 注意这里直接传递数组
    )
    return response.data
} 