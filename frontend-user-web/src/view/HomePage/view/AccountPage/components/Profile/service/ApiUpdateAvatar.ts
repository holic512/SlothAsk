import axios from '@/axios/axios';

// 更新头像接口
export const apiUpdateAvatar = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file); // 将文件添加到FormData

    try {
        const response = await axios.put(
            'service-user/user/info/avatar',
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data', // 设置文件上传的 Content-Type
                },
            });

        return response.data; // 返回接口返回的数据
    } catch (error) {
        console.error('Error updating avatar:', error);
        throw error; // 或者根据需要返回自定义错误
    }
};
