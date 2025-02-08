import axios from "@/axios/axios"
import type {ISearchParams} from '../types/types'

/**
 * 获取分类列表
 */
export const apiGetCategoryList = async (params: ISearchParams) => {
    try {
        const response = await axios.get('service-question/admin/category/list', {
            params: {
                pageNum: params.pageNum,
                pageSize: params.pageSize,
                keyword: params.keyword,
                projectId: params.projectId,
                status: params.status
            }
        })

        return response.data
    } catch (error) {
        console.error('获取分类列表失败:', error)
        return {
            status: 500,
            message: '获取分类列表失败'
        }
    }
} 