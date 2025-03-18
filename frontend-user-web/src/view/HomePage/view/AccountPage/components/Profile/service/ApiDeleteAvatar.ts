import axios from '@/axios/axios';

// 更新头像接口
export const apiDeleteAvatar = async () => {
    try {
        const response = await axios.delete(
            'service-user/user/info/avatar');

        return response.data; // 返回接口返回的数据
    } catch (error) {
        console.error('Error updating avatar:', error);
        throw error; // 或者根据需要返回自定义错误
    }
};
