<template>
  <div class="online-count-display">
    <span class="count-text">
      当前学习人数: {{ onlineCount }}
    </span>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, onUnmounted, ref} from 'vue'
import {getDisplayOnlineCount} from './service/onlineCountApi'

const onlineCount = ref<number>(0)
let intervalId: number | null = null

/**
 * 获取在线人数
 */
const fetchOnlineCount = async () => {
  try {
    const response = await getDisplayOnlineCount()
    if (response.status === 200) {
      onlineCount.value = response.data
    }
  } catch (error) {
    console.error('获取在线人数失败:', error)
  }
}

/**
 * 组件挂载时开始定时获取数据
 */
onMounted(() => {
  // 立即获取一次
  fetchOnlineCount()
  
  // 每1分钟更新一次
  intervalId = setInterval(fetchOnlineCount, 60000)
})

/**
 * 组件卸载时清除定时器
 */
onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId)
  }
})
</script>

<style scoped>
.online-count-display {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 12px;
  backdrop-filter: blur(4px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  user-select: none;
}

.online-count-display:hover {
  background: rgba(0, 0, 0, 0.8);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.count-text {
  font-weight: 500;
  letter-spacing: 0.5px;
}
</style>