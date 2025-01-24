import axios from "@/axios/axios";

export const apiDeleteBatchUsers = async (ids: number[]) => {
    const response = await axios.delete(
        'service-user/admin/delete/batchUsers',
        {
            data: {
                ids: ids,
            }
        }
    );
    return response.data;
}