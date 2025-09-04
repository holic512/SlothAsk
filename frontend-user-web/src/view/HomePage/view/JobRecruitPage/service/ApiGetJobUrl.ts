/**
 * 获取岗位申请URL接口
 * 对接后端 /job-recruit/get-url 接口
 */
import axios from '@/axios/axios';

/**
 * 岗位URL查询请求参数
 */
interface JobUrlQueryRequest {
    /** 岗位ID */
    jobId: number;
}

/**
 * 岗位URL响应数据
 */
interface JobUrlResponse {
    /** 申请URL */
    applyUrl: string;
}

/**
 * API响应格式
 */
interface ApiResponse<T> {
    code: number;
    message: string;
    data: T;
    status: number;
}

/**
 * 获取岗位申请URL（仅VIP用户可访问）
 * @param request 请求参数对象
 * @returns Promise<JobUrlResponse>
 */
export const getJobUrl = async (request: JobUrlQueryRequest): Promise<JobUrlResponse> => {
    try {
        const requestData: JobUrlQueryRequest = {
            jobId: request.jobId
        };
        
        // 发送POST请求
        const response = await axios.post<ApiResponse<JobUrlResponse>>(
            'service-jobrecruitpage/job-recruit/get-url', 
            requestData
        );
        
        // 检查响应状态
        if (response.data.status === 200) {
            return response.data.data;
        } else {
            throw new Error(response.data.message || '获取申请URL失败');
        }
    } catch (error: any) {
        // 处理特定的VIP权限错误
        if (error.response?.data?.code === 403) {
            throw new Error(error.response.data.message || '仅VIP用户可以获取申请链接');
        }
        
        // 处理网络错误或其他错误
        if (error.response) {
            throw new Error(error.response.data?.message || '服务器错误');
        } else if (error.request) {
            throw new Error('网络连接失败，请检查网络设置');
        } else {
            throw new Error(error.message || '获取申请URL失败');
        }
    }
};

export default getJobUrl;