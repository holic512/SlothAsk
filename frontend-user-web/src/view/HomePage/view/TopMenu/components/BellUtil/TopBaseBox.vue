<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {Check, View} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import DynamicMessageComponent from '@/view/HomePage/components/Message/DynamicMessageComponent.vue'
import type {BaseMessage} from '@/view/HomePage/components/Message/MessageInterface'
import {MessageDisplayMode} from '@/view/HomePage/components/Message/MessageInterface'
import {
  checkUnreadMessagesInLatestTen,
  getLatestTenMessages,
  markLatestTenMessagesAsRead
} from '@/view/HomePage/view/TopMenu/Api/ApiBaseMessage'

const router = useRouter()

// 响应式数据
const messages = ref<BaseMessage[]>([])
const loading = ref(false)
const hasUnreadMessages = ref(false)

// 获取最新十条消息
const fetchLatestMessages = async () => {
  try {
    loading.value = true
    const response = await getLatestTenMessages()
    if (response.status === 200) {
      messages.value = response.data || []
    } else {
      ElMessage.error(response.message || '获取消息失败')
    }
    console.log(messages)
  } catch (error) {
    console.error('获取消息失败:', error)
    ElMessage.error('获取消息失败')
  } finally {
    loading.value = false
  }
}

// 检查是否有未读消息
const checkUnreadMessages = async () => {
  try {
    const response = await checkUnreadMessagesInLatestTen()
    if (response.status === 200) {
      hasUnreadMessages.value = response.data || false
    }
  } catch (error) {
    console.error('检查未读消息失败:', error)
  }
}

// 一键已读
const markAllAsRead = async () => {
  try {
    const response = await markLatestTenMessagesAsRead()
    if (response.status === 200) {
      ElMessage.success(`已读成功，共更新 ${response.data} 条消息`)
      // 刷新消息列表
      await fetchLatestMessages()
      await checkUnreadMessages()
    } else {
      ElMessage.error(response.message || '标记已读失败')
    }
  } catch (error) {
    console.error('标记已读失败:', error)
    ElMessage.error('标记已读失败')
  }
}

// 查看全部消息
const viewAll = () => {
  router.push('/messages')
}

// 刷新消息方法（供父组件调用）
const refreshMessages = async () => {
  await fetchLatestMessages()
  await checkUnreadMessages()
}

// 暴露方法给父组件
defineExpose({
  refreshMessages
})

// 组件挂载时获取数据
onMounted(async () => {
  await fetchLatestMessages()
  await checkUnreadMessages()
})
</script>

<template>
  <div class="top-base-box">
    <!-- 顶部操作栏 -->
    <div class="header-actions">
      <el-button 
        class="action-btn"
        size="small" 
        type="text"
        @click="viewAll"
      >
        <el-icon><View /></el-icon>
        查看全部
      </el-button>
      
      <el-button 
        :disabled="!hasUnreadMessages"
        class="action-btn"
        size="small"
        type="text"
        @click="markAllAsRead"
      >
        <el-icon><Check /></el-icon>
        一键已读
      </el-button>
    </div>
    
    <!-- 消息列表 -->
    <div v-loading="loading" class="message-list">
      <div v-if="messages.length === 0 && !loading" class="empty-state">
        <p>暂无消息</p>
      </div>
      
      <div v-else class="message-scroll">
        <div 
          v-for="message in messages" 
          :key="message.messageId"
          class="message-item"
        >
          <DynamicMessageComponent 
            :display-mode="MessageDisplayMode.MINI_WINDOW"
            :message="message"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.top-base-box {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #e4e7ed;
  background: #fafafa;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
  font-size: 14px;
  transition: all 0.2s ease;
}

.action-btn:hover {
  background-color: #f0f0f0;
  color: #409eff;
}

.action-btn:disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.action-btn:disabled:hover {
  background-color: transparent;
  color: #c0c4cc;
}

.message-list {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #909399;
  font-size: 14px;
}

.empty-state p {
  margin: 0;
}

.message-scroll {
  height: 100%;
  overflow-y: auto;

}

.message-scroll::-webkit-scrollbar {
  width: 6px;
}

.message-scroll::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.message-scroll::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.message-scroll::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.message-item {
}

.message-item:last-child {
  margin-bottom: 0;
}

/* 加载状态样式 */
:deep(.el-loading-mask) {
  border-radius: 8px;
}
</style>