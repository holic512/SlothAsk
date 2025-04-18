<template>
  <div class="reply-card">
    <el-avatar :src="reply.avatar" :size="32" class="user-avatar" />

    <div class="reply-body">
      <div class="reply-header">
        <span class="user-name">{{ reply.nickname }}</span>
        <span class="reply-time">{{ formattedTime }}</span>
      </div>
      
      <div class="reply-content">{{ reply.content }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { ReplyType } from '../types';
import { useTimeFormat } from '../composables/useTimeFormat';

const props = defineProps<{
  reply: ReplyType;
}>();

// 使用可复用的时间格式化功能
const { formatTime } = useTimeFormat();
const formattedTime = computed<string>(() => formatTime(props.reply.createTime));
</script>

<style scoped>
.reply-card {
  padding: 4px 0;
  display: flex;
  gap: 12px;
}

.reply-body {
  flex: 1;
}

.reply-header {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
}

.user-name {
  font-size: 14px; /* 增大字体 */
  font-weight: 500;
  color: #333;
  margin-right: 8px;
}

.reply-time {
  font-size: 13px; /* 增大字体 */
  color: #999;
}

.reply-content {
  font-size: 14px; /* 增大字体 */
  line-height: 1.5;
  color: #333;
  white-space: pre-wrap;
  word-break: break-word;
}
</style> 