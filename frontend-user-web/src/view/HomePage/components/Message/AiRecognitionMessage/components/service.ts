import {ElMessage} from 'element-plus'
import type {AiRecognitionMessage} from '../index'
import {markMessageAsRead} from "@/view/HomePage/components/Message/service/messageApi";

/**
 * 根据准确率获取对应的颜色
 * @param accuracy 准确率（0-100）
 * @returns 颜色值
 */
export const getAccuracyColor = (accuracy: number): string => {
    if (accuracy >= 80) return '#67C23A'
    if (accuracy >= 60) return '#E6A23C'
    return '#F56C6C'
}


/**
 * 处理跳转题目的逻辑 - 内部封装版本
 * @param message AI识别消息对象
 * @param router 路由对象
 */
export const handleJumpToQuestionInternal = async (message: AiRecognitionMessage, router) => {
    if (message.messageData.questionId) {
        try {
            // 先标记消息为已读
            await markMessageAsRead(message.messageId)
            // 然后跳转到题目页面
            router.push(`/question/${message.messageData.questionId}`)
        } catch (error) {
            // 即使标记已读失败，也继续跳转
            console.warn('标记消息已读失败，但继续跳转:', error)
            router.push(`/question/${message.messageData.questionId}`)
        }
    } else {
        ElMessage.warning('题目ID不存在')
    }
}

/**
 * 处理跳转题目的逻辑 - 事件发射版本（保持向后兼容）
 * @param message AI识别消息对象
 * @param emit Vue组件的emit函数
 */
export const handleJumpToQuestion = async (
    message: AiRecognitionMessage,
    emit: (event: 'jumpToQuestion', questionId: string) => void
) => {
    if (message.messageData.questionId) {
        try {
            // 先标记消息为已读
            await markMessageAsRead(message.messageId)
            // 然后发射跳转事件
            emit('jumpToQuestion', message.messageData.questionId)
        } catch (error) {
            // 即使标记已读失败，也继续发射事件
            console.warn('标记消息已读失败，但继续跳转:', error)
            emit('jumpToQuestion', message.messageData.questionId)
        }
    } else {
        ElMessage.warning('题目ID不存在')
    }
}

/**
 * 格式化时间显示
 * @param time 时间字符串
 * @returns 格式化后的时间字符串
 */
export const formatTime = (time: string): string => {
    if (!time) return ''

    const now = new Date()
    const targetTime = new Date(time)
    const diffMs = now.getTime() - targetTime.getTime()
    const diffMinutes = Math.floor(diffMs / (1000 * 60))
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60))
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24))

    // 1分钟内
    if (diffMinutes < 1) {
        return '刚刚'
    }
    // 1小时内
    else if (diffMinutes < 60) {
        return `${diffMinutes}分钟前`
    }
    // 24小时内
    else if (diffHours < 24) {
        return `${diffHours}小时前`
    }
    // 7天内
    else if (diffDays < 7) {
        return `${diffDays}天前`
    }
    // 超过7天，显示具体日期
    else {
        const currentYear = now.getFullYear()
        const targetYear = targetTime.getFullYear()

        // 同一年内，不显示年份
        if (currentYear === targetYear) {
            return targetTime.toLocaleString('zh-CN', {
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            })
        }
        // 不同年份，显示完整日期
        else {
            return targetTime.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            })
        }
    }
}

/**
 * AI识别消息组件的通用工具函数集合
 */
export const AiRecognitionMessageService = {
    getAccuracyColor,
    handleJumpToQuestion,
    handleJumpToQuestionInternal,
    formatTime
}

export default AiRecognitionMessageService