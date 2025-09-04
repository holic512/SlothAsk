/**
 * 更新申请状态接口
 * 对接后端 /job-recruit/update-status 接口
 */
import axios from '@/axios/axios';
import type { ApplicationStatus } from '../type/JobItem';

/**
 * 更新申请状态请求参数
 */
interface UpdateApplicationStatusRequest {
    /** 岗位ID */
    jobId: string;
    /** 新的申请状态 */
    applicationStatus: ApplicationStatus;
    /** 备注信息（可选） */
    remark?: string;
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
 * 更新申请状态
 * @param jobId 岗位ID
 * @param applicationStatus 新的申请状态
 * @param remark 备注信息（可选）
 * @returns Promise<boolean>
 */
export const updateApplicationStatus = async (
    jobId: string, 
    applicationStatus: ApplicationStatus, 
    remark?: string
): Promise<boolean> => {
    try {
        // 构建请求参数
        const requestParams: UpdateApplicationStatusRequest = {
            jobId,
            applicationStatus,
            remark
        };

        // 发送POST请求
        const response = await axios.post<ApiResponse<boolean>>('service-jobrecruitpage/job-recruit/update-status', requestParams);
        
        // 检查响应状态
        if (response.data.status === 200) {
            return response.data.data;
        } else {
            throw new Error(response.data.message || '更新申请状态失败');
        }
    } catch (error: any) {
        console.error('更新申请状态失败:', error);
        
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
            throw new Error(error.message || '更新申请状态失败');
        }
    }
};

/**
 * 批量更新申请状态
 * @param updates 批量更新参数数组
 * @returns Promise<boolean[]>
 */
export const batchUpdateApplicationStatus = async (
    updates: Array<{
        jobId: string;
        applicationStatus: ApplicationStatus;
        remark?: string;
    }>
): Promise<boolean[]> => {
    try {
        // 并发执行多个更新请求
        const promises = updates.map(update => 
            updateApplicationStatus(update.jobId, update.applicationStatus, update.remark)
        );
        
        const results = await Promise.allSettled(promises);
        
        // 处理结果，将成功的返回true，失败的返回false
        return results.map(result => {
            if (result.status === 'fulfilled') {
                return result.value;
            } else {
                console.error('批量更新中的单个请求失败:', result.reason);
                return false;
            }
        });
    } catch (error: any) {
        console.error('批量更新申请状态失败:', error);
        throw new Error('批量更新申请状态失败');
    }
};

export default updateApplicationStatus;