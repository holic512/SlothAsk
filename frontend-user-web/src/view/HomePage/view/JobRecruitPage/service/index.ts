/**
 * JobRecruitPage Service 统一导出
 * 提供岗位招聘页面相关的所有API接口
 */

// 导出获取岗位列表相关接口
export { getJobList } from './ApiGetJobList';

// 导出更新申请状态相关接口
export { 
    updateApplicationStatus, 
    batchUpdateApplicationStatus 
} from './ApiUpdateApplicationStatus';

// 默认导出所有接口
export default {
    getJobList: () => import('./ApiGetJobList').then(m => m.getJobList),
    updateApplicationStatus: () => import('./ApiUpdateApplicationStatus').then(m => m.updateApplicationStatus),
    batchUpdateApplicationStatus: () => import('./ApiUpdateApplicationStatus').then(m => m.batchUpdateApplicationStatus)
};