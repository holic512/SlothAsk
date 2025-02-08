import axios from "@/axios/axios"

/**
 * 项目选项接口
 */
export interface IProjectOption {
  id: number
  name: string
  description: string
}

/**
 * 获取项目选项列表
 */
export const apiGetProjectOptions = async () => {
  try {
    const response = await axios.get('service-question/admin/category/project/options')
    
    return response.data
  } catch (error) {
    console.error('获取项目选项失败:', error)
    return {
      status: 500,
      message: '获取项目选项失败'
    }
  }
} 