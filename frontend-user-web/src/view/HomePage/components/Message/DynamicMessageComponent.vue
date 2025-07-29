<script lang="ts" setup>
import {defineAsyncComponent, computed, type Component} from 'vue'
import {Warning} from '@element-plus/icons-vue'
import type {BaseMessage} from './MessageInterface'
import {MessageType, MessageDisplayMode} from './MessageInterface'

// 组件属性定义
interface Props {
  message: BaseMessage
  displayMode: MessageDisplayMode
}

const props = defineProps<Props>()

// 组件映射配置
const componentMap = {
  [MessageType.AI_RECOGNITION]: {
    [MessageDisplayMode.TOAST]: () => import('./AiRecognitionMessage/components/ToastMessage.vue'),
    [MessageDisplayMode.MINI_WINDOW]: () => import('./AiRecognitionMessage/components/MiniWindowMessage.vue'),
    [MessageDisplayMode.FULL_PAGE]: () => import('./AiRecognitionMessage/components/FullPageMessage.vue')
  },
  // // 预留其他消息类型的位置
  // [MessageType.USER_FOLLOW]: {
  //   [MessageDisplayMode.TOAST]: () => import('./UserFollowMessage/components/ToastMessage.vue'),
  //   [MessageDisplayMode.MINI_WINDOW]: () => import('./UserFollowMessage/components/MiniWindowMessage.vue'),
  //   [MessageDisplayMode.FULL_PAGE]: () => import('./UserFollowMessage/components/FullPageMessage.vue')
  // },
  // [MessageType.COMMENT_REPLY]: {
  //   [MessageDisplayMode.TOAST]: () => import('./CommentReplyMessage/components/ToastMessage.vue'),
  //   [MessageDisplayMode.MINI_WINDOW]: () => import('./CommentReplyMessage/components/MiniWindowMessage.vue'),
  //   [MessageDisplayMode.FULL_PAGE]: () => import('./CommentReplyMessage/components/FullPageMessage.vue')
  // }
}

// 动态组件计算属性
const DynamicComponent = computed(() => {
  const messageType = props.message.type
  const displayMode = props.displayMode

  // 获取对应的组件加载函数
  const componentLoader = componentMap[messageType]?.[displayMode]

  if (!componentLoader) {
    console.warn(`未找到消息类型 ${messageType} 和显示模式 ${displayMode} 对应的组件`)
    return null
  }

  // 使用 defineAsyncComponent 进行懒加载
  return defineAsyncComponent({
    loader: componentLoader,
    // 加载中显示的组件
    loadingComponent: {
      template: '<div class="message-loading">加载中...</div>'
    },
    // 加载失败显示的组件
    errorComponent: {
      template: '<div class="message-error">组件加载失败</div>'
    },
    // 延迟显示加载组件的时间
    delay: 200,
    // 超时时间
    timeout: 3000
  })
})

// 检查组件是否存在
const hasComponent = computed(() => {
  return !!componentMap[props.message.type]?.[props.displayMode]
})
</script>

<template>
  <div class="dynamic-message-wrapper">
    <!-- 动态加载的消息组件 -->
    <component
        :is="DynamicComponent"
        v-if="hasComponent && DynamicComponent"
        :message="message"
    />

    <!-- 未找到对应组件时的占位符 -->
    <div v-else class="message-placeholder">
      <div class="placeholder-content">
        <el-icon class="placeholder-icon">
          <Warning/>
        </el-icon>
        <p class="placeholder-text">暂不支持此类型消息的显示</p>
        <p class="placeholder-detail">
          消息类型: {{ message.type }}, 显示模式: {{ displayMode }}
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dynamic-message-wrapper {
  width: 100%;
}

.message-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  color: #909399;
  font-size: 14px;
}

.message-error {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  color: #F56C6C;
  font-size: 14px;
  background: #FEF0F0;
  border: 1px solid #FBC4C4;
  border-radius: 4px;
}

.message-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: #FAFAFA;
  border: 1px dashed #DCDFE6;
  border-radius: 8px;
  text-align: center;
}

.placeholder-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.placeholder-icon {
  font-size: 24px;
  color: #E6A23C;
}

.placeholder-text {
  margin: 0;
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.placeholder-detail {
  margin: 0;
  font-size: 12px;
  color: #C0C4CC;
}
</style>