import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSessionStore = defineStore('Session', () => {
    // 使用 `ref` 来确保 `session` 是响应式的
    const session = ref({});

    // 获取 session
    const getSession = () => {
        return session.value;
    }

    // 设置 session
    const setSession = (newSession) => {
        session.value = newSession; // 直接修改 `session.value`
    }

    return {
        session,
        getSession,
        setSession
    };
}, {
    persist: true // 启用持久化
});
