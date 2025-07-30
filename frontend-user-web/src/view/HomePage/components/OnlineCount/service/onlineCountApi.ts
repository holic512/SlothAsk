import axios from '@/axios/axios'

/**
 * 获取展示用的在线人数
 * @returns {Promise} API响应
 */
export const getDisplayOnlineCount = async () => {
    try {
        const response = await axios.get('service-notification/baseMessage/sse/display-online-count')
        return response.data
    } catch (error) {
        console.error('获取在线人数失败:', error)
        throw error
    }
}

/**
 * 在线人数相关API服务
 */
export const OnlineCountApiService = {
    getDisplayOnlineCount
}

export default OnlineCountApiService