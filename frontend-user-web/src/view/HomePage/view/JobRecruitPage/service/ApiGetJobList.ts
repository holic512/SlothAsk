/**
 * 获取岗位列表接口
 * 对接后端 /job-recruit/list 接口
 */
import axios from '@/axios/axios';
import type { JobItem, JobListQuery } from '../type/JobItem';

/**
 * 岗位列表响应数据
 */
interface JobListResponse {
    /** 岗位列表 */
    jobs: JobItem[];
    /** 总记录数 */
    total: number;
    /** 当前页码 */
    page: number;
    /** 每页数量 */
    pageSize: number;
    /** 总页数 */
    totalPages: number;
}

/**
 * API响应格式
 */
interface ApiResponse<T> {
    code: number;
    message: string;
    data: T;
}

/**
 * 获取岗位列表
 * @param params 查询参数
 * @returns Promise<JobListResponse>
 */
export const getJobList = async (params: JobListQuery): Promise<JobListResponse> => {
    try {
        // 直接使用 JobListQuery 接口作为请求参数
        // 发送POST请求
        const response = await axios.post<ApiResponse<JobListResponse>>('service-jobrecruitpage/job-recruit/list', params);
        
        // 检查响应状态
        if (response.data.status === 200) {
            return response.data.data;
        } else {
            throw new Error(response.data.message || '获取岗位列表失败');
        }
    } catch (error: any) {
        console.error('获取岗位列表失败:', error);
        
        // 处理不同类型的错误
        if (error.response) {
            // 服务器响应错误
            const errorMessage = error.response.data?.message || '服务器错误';
            throw new Error(errorMessage);
        } else if (error.request) {
            // 网络错误
            throw new Error('网络连接失败，请检查网络设置');
        } else {
            // 其他错误
            throw new Error(error.message || '获取岗位列表失败');
        }
    }
};

export default getJobList;