import axios from '@/axios/axios'

export const ApiUpdateCategory = async (form: any, avatar?: File) => {
    // 创建 FormData
    const formData = new FormData()
    formData.append('metadata', new Blob([JSON.stringify(form)], { type: 'application/json' }))
    
    // 如果有新的头像文件，则添加到 FormData
    if (avatar) {
        formData.append('file', avatar)
    }

    const response = await axios.put(
        'service-question/admin/category/update',
        formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        }
    )

    return response.data
} 