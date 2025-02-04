import axios from "@/axios/axios";

export const ApiDeleteTag = async (id: number) => {
    const response = await axios.delete(
        `service-question/admin/tags/delete/${id}`
    )
    return response.data
} 