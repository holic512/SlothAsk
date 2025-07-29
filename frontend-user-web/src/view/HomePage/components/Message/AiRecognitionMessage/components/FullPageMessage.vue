<!-- 完整版本 -->
<script lang="ts" setup>
import {computed} from 'vue'
import {Check, TrendCharts, ArrowRight} from '@element-plus/icons-vue'
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
  <div class="ai-message-full">
    <div class="full-content">
      <!-- 未读红点 -->
      <div v-if="message.readStatus === 0" class="unread-dot"></div>
      
      <div class="full-title">
        <el-icon class="full-title-icon">
          <Check/>
        </el-icon>
        <span class="full-title-text">AI题目解析完成</span>
      </div>
      <div class="full-body">
        <div class="full-left">
          <div class="question-info">
            <h4 class="question-title">{{ message.messageData.questionTitle }}</h4>
            <div class="meta-info">
              <span class="time">{{ getRelativeTime(message.createTime) }}</span>
              <div class="accuracy">
                <el-icon>
                  <TrendCharts/>
                </el-icon>

                <span :style="{ color: accuracyColor }">正确率: {{ message.messageData.accuracy }}%</span>
              </div>
            </div>
          </div>
        </div>
        <div class="full-right">
          <el-button
              size="small"
              type="primary"
              @click="handleJumpToQuestion"
          >
            <el-icon>
              <ArrowRight/>
            </el-icon>
            跳转题目
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 完整版本样式 */
.ai-message-full {
}

.full-content {
  position: relative;
  background: white;
  border-radius: 8px;
  padding: 16px;
  transition: background-color 0.2s ease;
}

.full-content::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 10px;
  right: 10px;
  height: 1px;
  background-color: #e4e7ed;
}

.full-content:hover {
  background-color: #F7F7F7;
}

.full-content:hover::after {
  display: none;
}

.full-title {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 12px;
}

.full-title-icon {
  color: #67c23a;
  font-size: 16px;
}

.full-title-text {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.full-body {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.full-left {
  flex: 1;
  min-width: 0;
}

.full-left .question-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 6px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.time {
  font-size: 12px;
  color: #C0C4CC;
}

.accuracy {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 500;
}

.full-right {
  display: flex;
  align-items: center;
  gap: 8px;
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