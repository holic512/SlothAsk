import axios from "@/axios/axios";

export const apiDeleteUser = async (id: number) => {
    const response = await axios.delete(
        `service-user/admin/delete/${id}`
    );
    return response.data;
}