import {useSessionStore} from '@/pinia/Session'

/**
 * 检查用户当前是否已登录
 * @returns {boolean} 如果用户已登录则返回 true，否则返回 false
 */
export function isUserLoggedIn() {
    const userSession = useSessionStore()
    return Boolean(userSession.userSession && userSession.userSession.tokenValue)
}