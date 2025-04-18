<template>
  <div class="comment-card">
    <el-avatar :src="comment.avatar" :size="40" class="user-avatar" />
    
    <div class="comment-body">
      <div class="comment-header">
        <span class="user-name">{{ comment.nickname }}</span>
        <span class="comment-time">{{ formattedTime }}</span>
      </div>
      
      <div class="comment-content">{{ comment.content }}</div>
      
      <div class="comment-actions">
        <el-button 
          text 
          class="action-btn" 
          :class="{ 'liked': comment.isLiked }"
          @click="onLike"
        >
          <el-icon><IconThumbsUp /></el-icon>
          <span>{{ comment.likeCount > 0 ? comment.likeCount : '点赞' }}</span>
        </el-button>
        
        <el-button text class="action-btn" @click="showReplyForm = !showReplyForm">
          <el-icon><ChatLineRound /></el-icon>
          <span>{{ showReplyForm ? '取消回复' : '回复' }}</span>
        </el-button>
        
        <el-dropdown trigger="click" v-if="isAuthor">
          <el-button text class="action-btn">
            <el-icon><MoreFilled /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>编辑</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      
      <!-- 回复表单 -->
      <reply-form
        v-if="showReplyForm"
        :comment-id="comment.id"
        @submit="handleReplySubmit"
        @cancel="showReplyForm = false"
      />
      
      <!-- 回复列表 -->
      <reply-list
        v-if="comment.replies && comment.replies.length > 0"
        :replies="comment.replies"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, defineComponent, h } from 'vue';
import { MoreFilled, ChatLineRound } from '@element-plus/icons-vue';
import ReplyForm from './ReplyForm.vue';
import ReplyList from './ReplyList.vue';
import type { CommentType } from '../types';
import { useTimeFormat } from '../composables/useTimeFormat';

// 自定义点赞图标组件 - 使用 defineComponent
const IconThumbsUp = defineComponent({
  name: 'IconThumbsUp',
  setup() {
    return () => h('svg', 
      { 
        viewBox: '0 0 24 24', 
        width: '1em', 
        height: '1em' 
      }, 
      [
        h('path', { 
          fill: 'currentColor',
          d: 'M9 21h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-2c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.58 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2zM9 9l4.34-4.34L12 10h9v2l-3 7H9V9zM1 9h4v12H1z'
        })
      ]
    );
  }
});

const props = defineProps<{
  comment: CommentType;
}>();

const emit = defineEmits<{
  (e: 'reply', data: { commentId: number; content: string }): void;
  (e: 'like', commentId: number): void;
}>();

const showReplyForm = ref<boolean>(false);

// 模拟用户登录状态
const currentUserId = 999; // 示例用户ID

const isAuthor = computed<boolean>(() => {
  return props.comment.userId === currentUserId;
});

// 使用可复用的时间格式化功能
const { formatTime } = useTimeFormat();
const formattedTime = computed<string>(() => formatTime(props.comment.createTime));

const handleReplySubmit = (content: string): void => {
  emit('reply', { commentId: props.comment.id, content });
  showReplyForm.value = false;
};

const onLike = (): void => {
  if (!props.comment.isLiked) {
    emit('like', props.comment.id);
  }
};
</script>

<style scoped>
.comment-card {
  padding: 24px 32px; /* 增大内边距与问题详情一致 */
  display: flex;
  gap: 16px; /* 增大间距 */
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.1s;
}

.comment-card:hover {
  background-color: #f9f9f9;
}

.comment-body {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px; /* 增大间距 */
}

.user-name {
  font-weight: 600;
  font-size: 15px; /* 增大字体 */
  color: #333;
  margin-right: 8px;
}

.comment-time {
  font-size: 13px; /* 增大字体 */
  color: #999;
}

.comment-content {
  font-size: 15px; /* 增大字体 */
  line-height: 1.6;
  margin: 12px 0; /* 增大间距 */
  white-space: pre-wrap;
  word-break: break-word;
}

.comment-actions {
  display: flex;
  gap: 16px;
  margin-top: 12px; /* 增大间距 */
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px; /* 增大间距 */
  font-size: 14px; /* 增大字体 */
  color: #606266;
  padding: 6px 10px; /* 增大内边距 */
}

.action-btn.liked {
  color: #409eff;
}

.action-btn:hover {
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style> 