import {defineStore} from 'pinia';
import {ref} from 'vue';

// 定义用户会话的类型
interface Session {
    userId?: string;
    token?: string;
    email?: string;
    nickname?: string;
    avatar?: string;

    [key: string]: any; // 允许额外的字段
}

export const useSessionStore = defineStore('Session', () => {
    // 使用 `ref` 创建响应式 session
    const userSession = ref<Session>({});

    // 获取 session
    const getSession = (): Session => {
        return userSession.value;
    };

    // 设置 session
    const setSession = (newSession: Session) => {
        userSession.value = newSession;
    };

    // 更新用户信息
    const updateUserInfo = (nickname: string, avatar: string) => {
        userSession.value.nickname = nickname;
        userSession.value.avatar = avatar;
    };

    // 清除 session
    const clearSession = () => {
        userSession.value = {};
    };

    return {
        userSession,
        getSession,
        setSession,
        updateUserInfo,
        clearSession
    };
},{
    persist: true // 启用持久化
});


