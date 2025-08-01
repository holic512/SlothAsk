<script lang="ts" setup>
import {computed, onMounted, onUnmounted, ref} from 'vue'
import {Close} from '@element-plus/icons-vue'
import DynamicMessageComponent from './DynamicMessageComponent.vue'
import type {BaseMessage} from './MessageInterface'
import {MessageDisplayMode, MessageType} from './MessageInterface'
import type {AiRecognitionMessageData} from './AiRecognitionMessage/index'
import sseClient from "@/axios/sseClient"
import {SSEMessageType} from './SSEMessageType'
import {useSessionStore} from "@/pinia/Session";

// 消息列表
const messages = ref<BaseMessage[]>([])

// 消息消失时间配置（毫秒）
const MESSAGE_AUTO_REMOVE_DELAY = 5000

// 定时器引用
let messageTimer: NodeJS.Timeout | null = null
let autoRemoveTimers: Map<string, NodeJS.Timeout> = new Map()
// 记录被hover锁定的消息
const hoveredMessages = ref<Set<string>>(new Set())


// 添加新消息
const addMessage = (message: BaseMessage) => {
  // 如果消息数量超过3条，移除最旧的非锁定消息
  if (messages.value.length >= 3) {
    // 找到第一个非锁定的消息进行移除
    const messageToRemove = messages.value.find(msg => !hoveredMessages.value.has(msg.messageId))
    if (messageToRemove) {
      removeMessage(messageToRemove.messageId)
    } else {
      // 如果所有消息都被锁定，移除最旧的消息
      const oldestMessage = messages.value[0]
      removeMessage(oldestMessage.messageId)
    }
  }

  messages.value.push(message)

  // 设置自动移除
  const timer = setTimeout(() => {
    removeMessage(message.messageId)
  }, MESSAGE_AUTO_REMOVE_DELAY)

  autoRemoveTimers.set(message.messageId, timer)
}

// 移除消息
const removeMessage = (messageId: string) => {
  const index = messages.value.findIndex(msg => msg.messageId === messageId)
  if (index > -1) {
    messages.value.splice(index, 1)
  }

  // 清除对应的定时器
  const timer = autoRemoveTimers.get(messageId)
  if (timer) {
    clearTimeout(timer)
    autoRemoveTimers.delete(messageId)
  }
}

// 手动关闭消息
const handleCloseMessage = (messageId: string) => {
  removeMessage(messageId)
}

// 处理鼠标悬浮事件
const handleMouseEnter = (messageId: string) => {
  hoveredMessages.value.add(messageId)
  // 暂停该消息的自动移除定时器
  const timer = autoRemoveTimers.get(messageId)
  if (timer) {
    clearTimeout(timer)
    autoRemoveTimers.delete(messageId)
  }
}

// 处理鼠标离开事件
const handleMouseLeave = (messageId: string) => {
  hoveredMessages.value.delete(messageId)
  // 重新设置自动移除定时器
  const timer = setTimeout(() => {
    removeMessage(messageId)
  }, MESSAGE_AUTO_REMOVE_DELAY)

  autoRemoveTimers.set(messageId, timer)
}


// 清除所有定时器
const clearAllTimers = () => {
  autoRemoveTimers.forEach(timer => clearTimeout(timer))
  autoRemoveTimers.clear()
}

// 处理业务消息
const handleBusinessMessage = (eventDataString: string) => {
  try {
    // 解析接收到的消息数据
    const eventData = JSON.parse(eventDataString)

    // 构造BaseMessage对象
    const baseMessage: BaseMessage = {
      messageId: eventData.id,
      type: eventData.type,
      readStatus: eventData.readStatus,
      createTime: eventData.createTime,
      messageData: null // 先设为null，后面根据type处理
    }

    // 根据消息类型处理messageData
    if (eventData.type === MessageType.AI_RECOGNITION) {
      // AI识别消息，需要解析messageData字符串并转换为AiRecognitionMessageData类型
      const aiData = JSON.parse(eventData.messageData)
      baseMessage.messageData = {
        questionId: aiData.questionId,
        questionTitle: aiData.questionTitle,
        accuracy: aiData.accuracy
      } as AiRecognitionMessageData
    } else if (eventData.type === MessageType.USER_FOLLOW) {
      // 用户关注消息
      baseMessage.messageData = JSON.parse(eventData.messageData)
    } else if (eventData.type === MessageType.COMMENT_REPLY) {
      // 评论回复消息
      baseMessage.messageData = JSON.parse(eventData.messageData)
    } else {
      // 其他类型消息，直接使用原始数据
      baseMessage.messageData = eventData.messageData
    }

    // 调用addMessage添加消息
    addMessage(baseMessage)

  } catch (error) {
    console.error('处理业务消息失败:', error)
  }
}

let connection
let heartbeatTimer: NodeJS.Timeout | null = null
const HEARTBEAT_TIMEOUT = 60000 // 30秒心跳超时

// 获取用户session pinia实例
const userSession = useSessionStore();

// 判断用户是否登录
const isLoggedIn = computed(() => {
  return userSession.userSession && userSession.userSession.tokenValue;
});

// 建立SSE连接
const establishConnection = () => {
  try {

    if (!isLoggedIn.value) return

    connection = sseClient.connect('service-notification/baseMessage/sse/connect')

    // 监听心跳消息
    connection.addEventListener(SSEMessageType.Heartbeat, (event) => {
      // console.log('收到心跳:', event.data)
      resetHeartbeatTimer()
    })

    // 监听业务消息
    connection.addEventListener(SSEMessageType.BaseMessage, (event) => {
      // console.log('收到业务消息:', event.data)
      handleBusinessMessage(event.data)
    })

    // 启动心跳监控
    resetHeartbeatTimer()
    console.log('SSE连接已建立')
  } catch (error) {
    console.error('建立SSE连接失败:', error)
    // 5秒后重试连接
    setTimeout(establishConnection, 5000)
  }
}

// 重置心跳定时器
const resetHeartbeatTimer = () => {
  if (heartbeatTimer) {
    clearTimeout(heartbeatTimer)
  }

  heartbeatTimer = setTimeout(() => {
    console.warn('心跳超时，重新建立连接')
    reconnect()
  }, HEARTBEAT_TIMEOUT)
}

// 重新连接
const reconnect = () => {
  if (connection) {
    connection.close()
  }
  if (heartbeatTimer) {
    clearTimeout(heartbeatTimer)
    heartbeatTimer = null
  }
  establishConnection()
}

// 组件挂载时建立连接
onMounted(() => {
  establishConnection()
})

// 组件卸载时清理资源
onUnmounted(() => {
  if (connection) {
    connection.close()
  }
  if (heartbeatTimer) {
    clearTimeout(heartbeatTimer)
  }
  clearAllTimers()
})

</script>

<template>
  <div class="message-notification-container">
    <TransitionGroup class="message-list" name="message" tag="div">
      <div
          v-for="message in messages"
          :key="message.messageId"
          class="message-item"
          @mouseenter="handleMouseEnter(message.messageId)"
          @mouseleave="handleMouseLeave(message.messageId)"
      >
        <div class="message-content">
          <DynamicMessageComponent
              :display-mode="MessageDisplayMode.TOAST"
              :message="message"
          />
        </div>
        <button
            :aria-label="'关闭消息'"
            class="close-btn"
            @click="handleCloseMessage(message.messageId)"
        >
          <el-icon>
            <Close/>
          </el-icon>
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.message-notification-container {
  position: fixed;
  top: 40px;
  right: 20px;
  z-index: 9999;
  max-width: 320px;
  width: 320px;
  pointer-events: none; /* 允许点击穿透到下层元素 */
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-item {
  position: relative;
  pointer-events: auto; /* 恢复消息项的点击事件 */
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

.close-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 20px;
  height: 20px;
  border: none;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  transition: all 0.2s ease;
  z-index: 10;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(4px);
}

.close-btn:hover {
  background: rgba(255, 255, 255, 1);
  color: #F56C6C;
  transform: scale(1.1);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

/* 进入和离开动画 */
.message-enter-active {
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.message-leave-active {
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.message-enter-from {
  opacity: 0;
  transform: translateX(100%) scale(0.8);
}

.message-leave-to {
  opacity: 0;
  transform: translateX(100%) scale(0.8);
}

.message-move {
  transition: transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .message-notification-container {
    top: 60px;
    right: 10px;
    left: 10px;
    max-width: none;
  }

  .message-item {
    margin: 0;
  }
}

/* 悬浮效果 - 背景颜色波浪动画 */
.message-item {
  position: relative;
  overflow: hidden;
}

.message-item::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
      90deg,
      transparent,
      rgba(64, 158, 255, 0.1),
      transparent
  );
  transition: left 0.6s ease;
  pointer-events: none;
}

.message-item:hover::after {
  left: 100%;
}
</style>