import {defineStore} from 'pinia'
import {ref} from 'vue'

// 管理是否存在未读消息（红点提示）状态的 Store
export const useUnreadMessageStore = defineStore('UnreadMessage', () => {
    // 使用 ref 创建响应式的未读状态，默认 false
    const hasUnread = ref(false)

    // 获取未读状态
    const getHasUnread = (): boolean => {
        return hasUnread.value
    }

    // 设置未读状态
    const setHasUnread = (value: boolean) => {
        hasUnread.value = value
    }

    // 标记为有未读消息
    const markAsUnread = () => {
        hasUnread.value = true
    }

    // 标记为全部已读
    const markAllAsRead = () => {
        hasUnread.value = false
    }

    return {
        hasUnread,
        getHasUnread,
        setHasUnread,
        markAsUnread,
        markAllAsRead
    }
})
