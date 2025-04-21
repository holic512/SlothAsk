<template>
  <div class="recursive-reply-list">
    <div v-for="reply in replies" :key="reply.id" class="reply-thread">
      <reply-card
        :reply="reply"
        @like="handleLike"
        @reply="handleReply"
        @unlike="handleUnlike"
      />
      
      <div v-if="reply.children && reply.children.length > 0" class="nested-replies">
        <recursive-reply-list
          :replies="reply.children"
          @like="handleLike"
          @reply="handleReply"
          @unlike="handleUnlike"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {defineComponent} from 'vue';
import type {ReplyType} from '../types';

defineComponent({
  name: 'RecursiveReplyList'
});

const props = defineProps<{
  replies: ReplyType[];
}>();

const emit = defineEmits<{
  (e: 'like', replyId: number): void;
  (e: 'unlike', replyId: number): void;
  (e: 'reply', data: { replyId: number; content: string; replyToUserId: number }): void;
}>();

// 处理点赞
const handleLike = (replyId: number): void => {
  emit('like', replyId);
};

// 处理取消点赞
const handleUnlike = (replyId: number): void => {
  emit('unlike', replyId);
};

// 处理嵌套回复
const handleReply = (data: { replyId: number; content: string; replyToUserId: number }): void => {
  emit('reply', data);
};
</script>

<style scoped>
.recursive-reply-list {
  width: 100%;
}

.reply-thread {
  margin-bottom: 8px;
}

.nested-replies {
  margin-left: 20px;
  padding-left: 12px;
  border-left: 2px solid #ebeef5;
  margin-top: 4px;
}
</style> 