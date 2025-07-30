import axios from '@/axios/axios.js'

/**
 * 搜索相似题目
 * @param {string} queryText - 搜索关键词
 * @returns {Promise} 搜索结果
 */
export const searchSimilarQuestions = async (queryText) => {
    try {
        const response = await axios.post('service-question/user/search/similarity-search', null, {
            params: {
                queryText: queryText
            }
        })
        return response.data
    } catch (error) {
        console.error('搜索相似题目失败:', error)
        throw error
    }
}