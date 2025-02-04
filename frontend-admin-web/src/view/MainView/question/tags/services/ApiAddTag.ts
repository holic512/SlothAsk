import axios from "@/axios/axios";
import {TagCategory} from "@/view/MainView/question/tags/types/TagCategoryType";

export const ApiAddTag = async (formData: TagCategory) => {
    const response = await axios.post(
        'service-question/admin/tags/addtag',
        {
            project_id: formData.projectId,
            name: formData.name,
            description: formData.description,
            sort_order: formData.sortOrder,
            status: formData.status
        }
    )
    return response.data.status
}