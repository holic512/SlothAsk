import axios from "@/axios/axios";


// 用于上传 编辑题目的 上传图片的 接口
export const ApiPostUploadImage = async (file: File) => {

    const formData = new FormData();
    formData.append("file", file);


    const response = await axios.post(
        "service-question/admin/question/uploadImage",
        formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }
    )
    return response.data;
}