import axios from "@/axios/axios"

/**
 * 删除单个分类
 */
export const apiDeleteCategory = async (id: number) => {
    try {
        const response = await axios.delete(`service-question/admin/category/${id}`)
        return response.data
    } catch (error) {
        console.error('删除分类失败:', error)
        throw error
    }
}

/**
 * 批量删除分类
 */
export const apiBatchDeleteCategories = async (ids: number[]) => {
    try {
        // 修改请求方式，确保发送的是数组
        const response = await axios({
            method: 'delete',
            url: 'service-question/admin/category/batch',
            data: ids,
            headers: {
                'Content-Type': 'application/json'
            }
        })
        return response.data
    } catch (error) {
        console.error('批量删除分类失败:', error)
        throw error
    }
} 