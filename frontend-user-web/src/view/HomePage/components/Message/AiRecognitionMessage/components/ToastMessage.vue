<!-- 弹出层 -->
<script lang="ts" setup>
import {computed} from 'vue'
import {Check, TrendCharts} from '@element-plus/icons-vue'
import type {AiRecognitionMessage} from '../index'
import {getAccuracyColor, handleJumpToQuestionInternal} from './service'
import {useRouter} from "vue-router";

const router = useRouter()

// 组件属性定义
interface Props {
  message: AiRecognitionMessage
}

const props = defineProps<Props>()

// 计算属性
const accuracyColor = computed(() => {
  return getAccuracyColor(props.message.messageData.accuracy)
})

// 方法
const handleJumpToQuestion = () => {
  handleJumpToQuestionInternal(props.message, router)
}
</script>

<template>
  <div class="ai-message-toast">
    <div class="toast-header">
      <div class="header-left">
        <el-icon class="toast-icon">
          <Check/>
        </el-icon>
        <span class="toast-title">AI题目解析完成</span>
      </div>
      <div class="accuracy-info">
        <el-icon>
          <TrendCharts/>
        </el-icon>
        <span :style="{ color: accuracyColor }" class="accuracy-text">正确率: {{ message.messageData.accuracy }}%</span>
      </div>
    </div>
    <div class="toast-content">
      <p class="question-title">{{ message.messageData.questionTitle }}</p>
    </div>
    <div class="toast-actions">
      <el-button size="small" type="primary" @click="handleJumpToQuestion">跳转题目</el-button>
    </div>
  </div>
</template>

<style scoped>
/* 高级简洁的弹出框样式 */
.ai-message-toast {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  padding: 18px;
  max-width: 340px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.ai-message-toast:hover {
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.toast-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.toast-icon {
  color: #10b981;
  font-size: 20px;
  padding: 6px;
  background: rgba(16, 185, 129, 0.1);
  border-radius: 8px;
}

.toast-title {
  font-weight: 600;
  font-size: 16px;
  color: #1f2937;
  letter-spacing: -0.01em;
}

.toast-content {
  margin-bottom: 18px;
}

.toast-content .question-title {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-weight: 400;
}

.accuracy-info {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(99, 102, 241, 0.08);
  padding: 6px 10px;
  border-radius: 20px;
  border: 1px solid rgba(99, 102, 241, 0.15);
}

.accuracy-info .el-icon {
  color: #6366f1;
  font-size: 14px;
}

.accuracy-text {
  font-size: 12px;
  font-weight: 600;
  color: #6366f1;
  letter-spacing: 0.02em;
}

.toast-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.toast-actions .el-button {
  border-radius: 8px;
  font-weight: 500;
  letter-spacing: 0.01em;
  padding: 8px 16px;
  border: none;
  background: linear-gradient(135deg, #EBAB79 0%, #D4956B 100%);
  color: white;
  transition: all 0.2s ease;
}

.toast-actions .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(235, 171, 121, 0.4);
  background: linear-gradient(135deg, #E8A06B 0%, #D18A5A 100%);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-message-toast {
    max-width: 300px;
    padding: 14px;
  }
  
  .toast-header {
    margin-bottom: 10px;
  }
  
  .toast-content {
    margin-bottom: 14px;
  }
}
</style>