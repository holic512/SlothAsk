/**
 * 统一的岗位投递工具函数
 * 处理VIP检查、登录验证和投递逻辑
 */
import {ElMessage} from 'element-plus'
import {getJobUrl} from '../service'
import {isUserLoggedIn} from '@/utils/useIsLoggedIn'
import type {JobItem} from '../type/JobItem'

/**
 * 投递结果类型
 */
export interface ApplyJobResult {
  /** 是否成功 */
  success: boolean
  /** 错误类型 */
  errorType?: 'NOT_LOGGED_IN' | 'NOT_VIP' | 'NETWORK_ERROR' | 'OTHER'
  /** 错误消息 */
  errorMessage?: string
  /** 申请URL（成功时返回） */
  applyUrl?: string
}

/**
 * 统一的岗位投递函数
 * @param job 岗位信息
 * @returns Promise<ApplyJobResult>
 */
export const applyJob = async (job: JobItem): Promise<ApplyJobResult> => {
  try {
    // 1. 检查用户是否已登录
    if (!isUserLoggedIn()) {
      return {
        success: false,
        errorType: 'NOT_LOGGED_IN',
        errorMessage: '请先登录后再进行投递操作'
      }
    }

    // 2. 调用getJobUrl接口获取申请URL（包含VIP检查）
    const response = await getJobUrl({ jobId: job.id })
    
    // 3. 如果成功获取到URL，则打开新窗口
    if (response.applyUrl) {
      window.open(response.applyUrl, '_blank')
      ElMessage.success('投递成功，已为您打开申请页面')
      return {
        success: true,
        applyUrl: response.applyUrl
      }
    } else {
      return {
        success: false,
        errorType: 'OTHER',
        errorMessage: '获取申请链接失败'
      }
    }
  } catch (error: any) {
    console.error('投递失败:', error)
    
    // 处理VIP权限错误
    if (error.message?.includes('VIP') || error.message?.includes('仅VIP用户')) {
      return {
        success: false,
        errorType: 'NOT_VIP',
        errorMessage: error.message
      }
    }
    
    // 处理网络错误
    if (error.message?.includes('网络') || error.message?.includes('连接')) {
      return {
        success: false,
        errorType: 'NETWORK_ERROR',
        errorMessage: error.message
      }
    }
    
    // 其他错误
    return {
      success: false,
      errorType: 'OTHER',
      errorMessage: error.message || '投递失败，请稍后重试'
    }
  }
}

/**
 * 处理投递结果的通用函数
 * @param result 投递结果
 * @param onNotLoggedIn 未登录时的回调
 * @param onNotVip 非VIP时的回调
 */
export const handleApplyResult = (
  result: ApplyJobResult,
  onNotLoggedIn?: () => void,
  onNotVip?: () => void
) => {
  if (!result.success) {
    switch (result.errorType) {
      case 'NOT_LOGGED_IN':
        if (onNotLoggedIn) {
          onNotLoggedIn()
        } else {
          ElMessage.warning(result.errorMessage || '请先登录')
        }
        break
      case 'NOT_VIP':
        if (onNotVip) {
          onNotVip()
        } else {
          ElMessage.warning(result.errorMessage || '仅VIP用户可以投递')
        }
        break
      case 'NETWORK_ERROR':
        ElMessage.error(result.errorMessage || '网络连接失败')
        break
      default:
        ElMessage.error(result.errorMessage || '投递失败')
        break
    }
  }
}

/**
 * 简化的投递函数，自动处理错误提示
 * @param job 岗位信息
 * @param showLoginPrompt 显示登录提示弹窗的函数
 * @returns Promise<boolean> 是否成功
 */
export const applyJobWithAutoHandling = async (
  job: JobItem,
  showLoginPrompt?: () => void
): Promise<boolean> => {
  const result = await applyJob(job)
  
  handleApplyResult(result, showLoginPrompt)
  
  return result.success
}

export default {
  applyJob,
  handleApplyResult,
  applyJobWithAutoHandling
}