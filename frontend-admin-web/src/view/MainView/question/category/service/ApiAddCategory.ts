import axios from "@/axios/axios";

export const ApiAddCategory = async (form: any, avtar: File) => {
    // 打包formData
    const formData = new FormData();
    formData.append('metadata', new Blob([JSON.stringify(form)], {type: 'application/json'}));
    formData.append('file', avtar)

    const response = await axios.post(
        "service-question/admin/category/add",
        formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }
    )

    return response.data;
}