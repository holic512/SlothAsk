import axios from '@/axios/axios'

/**
 * 标记消息为已读
 * @param {string|number} messageId 消息ID
 * @returns {Promise} API响应
 */
export const markMessageAsRead = async (messageId) => {
    try {
        const response = await axios.post(`/user/basemessage/mark-read/${messageId}`)
        return response.data
    } catch (error) {
        console.error('标记消息已读失败:', error)
        throw error
    }
}

/**
 * 消息相关API服务
 */
export const MessageApiService = {
    markMessageAsRead
}

export default MessageApiService