<script lang="ts" setup>
import { ref } from 'vue'
import { ToastMessage, MiniWindowMessage, FullPageMessage } from './index'
import type { AiRecognitionMessage } from './index'
import { MessageType, ReadStatus } from '../MessageInterface'

// 示例数据
const sampleMessage1: AiRecognitionMessage = {
  messageId: 'msg001',
  type: MessageType.AI_RECOGNITION,
  readStatus: ReadStatus.UNREAD,
  createTime: '2024-01-15T10:30:00Z',
  messageData: {
    questionId: 'q001',
    questionTitle: '关于Vue 3组合式API的使用方法，下列说法正确的是？',
    accuracy: 85
  }
}

const sampleMessage2: AiRecognitionMessage = {
  messageId: 'msg002',
  type: MessageType.AI_RECOGNITION,
  readStatus: ReadStatus.UNREAD,
  createTime: '2024-01-15T09:15:00Z',
  messageData: {
    questionId: 'q002',
    questionTitle: 'JavaScript中的闭包概念及其应用场景',
    accuracy: 72
  }
}

const sampleMessage3: AiRecognitionMessage = {
  messageId: 'msg003',
  type: MessageType.AI_RECOGNITION,
  readStatus: ReadStatus.read,
  createTime: '2024-01-14T16:45:00Z',
  messageData: {
    questionId: 'q003',
    questionTitle: 'CSS Grid布局与Flexbox布局的区别和使用场景',
    accuracy: 91
  }
}

// 注意：跳转逻辑已经内部封装在组件中，不再需要外部事件处理
</script>

<template>
  <div class="demo-container">
    <h2>AI识别消息组件演示</h2>
  
    <div class="demo-section">
      <h4>4.1 Toast组件</h4>
      <div class="demo-item">
        <ToastMessage
          :message="sampleMessage1"
        />
      </div>

      <h4>4.2 MiniWindow组件</h4>
      <div class="demo-item">
        <MiniWindowMessage
          :message="sampleMessage2"
        />
      </div>

      <h4>4.3 FullPage组件</h4>
      <div class="demo-item">
        <FullPageMessage
          :message="sampleMessage3"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.demo-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.demo-container h2 {
  text-align: center;
  color: #303133;
  margin-bottom: 32px;
}

.demo-section {
  margin-bottom: 48px;
}

.demo-section h3 {
  color: #409EFF;
  margin-bottom: 8px;
  font-size: 18px;
}

.demo-desc {
  color: #606266;
  margin-bottom: 16px;
  font-size: 14px;
}

.demo-content {
  border: 1px solid #EBEEF5;
  border-radius: 8px;
  padding: 24px;
  background: #FAFAFA;
}

/* 为小框模式添加间距 */
.demo-content .ai-message-mini + .ai-message-mini {
  margin-top: 12px;
}
</style>