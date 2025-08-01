import {EventSourcePolyfill} from 'event-source-polyfill'
import {useSessionStore} from '@/pinia/Session'

// api环境变量
const baseUrl = import.meta.env.VITE_API_BASE_URL

// SSE连接对象类
class SSEConnection {
    constructor(eventSource) {
        this.eventSource = eventSource
    }

    /**
     * 添加事件监听器
     * @param {string} messageType - 需要监听的消息类型
     * @param {function} onMessage - 收到消息时的回调
     */
    addEventListener(messageType, onMessage) {
        if (this.eventSource) {
            this.eventSource.addEventListener(messageType, onMessage)
        }
    }

    /**
     * 移除事件监听器
     * @param {string} messageType - 需要移除的消息类型
     * @param {function} onMessage - 对应的回调函数
     */
    removeEventListener(messageType, onMessage) {
        if (this.eventSource) {
            this.eventSource.removeEventListener(messageType, onMessage)
        }
    }

    /**
     * 关闭连接
     */
    close() {
        if (this.eventSource) {
            this.eventSource.close()
            this.eventSource = null
            console.log('SSE连接已关闭')
        }
    }

    /**
     * 获取连接状态
     */
    getReadyState() {
        return this.eventSource ? this.eventSource.readyState : EventSource.CLOSED
    }
}

// 封装类
class SSEClient {
    /**
     * 建立 SSE 连接
     * @param {string} path - SSE 接口路径
     * @param {object} options - 连接选项
     * @returns {SSEConnection} - 返回连接对象
     */
    connect(path, options = {}) {
        const Session = useSessionStore()
        const token = Session?.userSession?.tokenValue

        const headers = {
            'Content-Type': 'text/event-stream',
            ...options.headers
        }

        if (token) {
            headers['SlothAsk'] = token
        }

        const fullUrl = `${baseUrl.replace(/\/$/, '')}/${path.replace(/^\//, '')}`

        const eventSourceOptions = {
            headers,
            heartbeatTimeout: options.heartbeatTimeout || 60000,
            withCredentials: options.withCredentials || false
        }

        const eventSource = new EventSourcePolyfill(fullUrl, eventSourceOptions)

        // 返回连接对象
        return new SSEConnection(eventSource)
    }
}

// 单例导出
const sseClient = new SSEClient()
export default sseClient
