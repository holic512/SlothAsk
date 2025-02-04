import axios from "@/axios/axios";
import { TagCategory } from "../types/TagCategoryType";

export const ApiUpdateTag = async (formData: TagCategory) => {
    const response = await axios.put(
        'service-question/admin/tags/update',
        {
            id: formData.id,
            projectId: formData.projectId,
            name: formData.name,
            description: formData.description,
            sortOrder: formData.sortOrder,
            status: formData.status
        }
    )
    return response.data
} 