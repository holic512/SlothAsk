<!-- 小窗 -->
<script lang="ts" setup>
import {computed} from 'vue'
import {Check, TrendCharts, ArrowRight} from '@element-plus/icons-vue'
import type {AiRecognitionMessage} from '../index'
import {getAccuracyColor, handleJumpToQuestionInternal, formatTime} from './service'
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

// 获取相对时间
const getRelativeTime = (timestamp: number | string) => {
  const now = Date.now()
  let timestampMs: number
  
  // 处理不同的时间戳格式
  if (typeof timestamp === 'string') {
    // 如果是字符串，尝试解析为Date
    timestampMs = new Date(timestamp).getTime()
  } else {
    // 如果是数字，检查是否为秒级时间戳（小于13位）
    timestampMs = timestamp < 10000000000 ? timestamp * 1000 : timestamp
  }
  
  const diff = now - timestampMs
  
  // 如果解析失败，返回默认值
  if (isNaN(diff)) {
    return '未知时间'
  }

  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  const months = Math.floor(days / 30)
  const years = Math.floor(days / 365)

  if (years > 0) {
    return `${years}年前`
  } else if (months > 0) {
    return `${months}个月前`
  } else if (days > 0) {
    return `${days}天前`
  } else if (hours > 0) {
    return `${hours}小时前`
  } else if (minutes > 0) {
    return `${minutes}分钟前`
  } else {
    return '刚刚'
  }
}
</script>

<template>
  <div class="ai-message-mini" @click="handleJumpToQuestion">
    <!-- 未读红点 -->
    <div v-if="message.readStatus === 0" class="unread-dot"></div>
    
    <div class="mini-content">
      <div class="top-row">
        <div class="mini-title">
          <el-icon class="mini-title-icon">
            <Check/>
          </el-icon>
          <span class="mini-title-text">AI题目解析完成</span>
        </div>
        <div class="accuracy">
          <el-icon>
            <TrendCharts/>
          </el-icon>
          <span :style="{ color: accuracyColor }">正确率: {{ message.messageData.accuracy }}%</span>
        </div>
        <div class="relative-time">{{ getRelativeTime(message.createTime) }}</div>
      </div>
      <div class="question-title">{{ message.messageData.questionTitle }}</div>
    </div>
  </div>
</template>

<style scoped>
/* 信息小框样式 */
.ai-message-mini {
  width: 400px;
  background-color: #ffffff;
  border-radius: 12px;
  padding: 16px 20px;
  cursor: pointer;
  transition: background-color 0.2s ease;
  position: relative;
}

.ai-message-mini::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 10px;
  right: 10px;
  height: 1px;
  background-color: #e4e7ed;
}

.ai-message-mini:hover {
  background-color: #F7F7F7;
}

.ai-message-mini:hover::after {
  display: none;
}

.mini-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.top-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mini-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.mini-title-icon {
  color: #67c23a;
  font-size: 16px;
}

.mini-title-text {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.accuracy {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #909399;
}

.accuracy .el-icon {
  font-size: 14px;
}

.question-title {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
  margin-top: 4px;
}

.relative-time {
  font-size: 12px;
  color: #c0c4cc;
  white-space: nowrap;
  margin-left: auto;
}

/* 未读红点样式 */
.unread-dot {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 8px;
  height: 8px;
  background-color: #f56c6c;
  border-radius: 50%;
  z-index: 10;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.7;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>