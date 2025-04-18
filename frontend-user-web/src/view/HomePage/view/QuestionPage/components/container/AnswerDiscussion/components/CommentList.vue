<template>
  <div class="comment-list">
    <comment-card
      v-for="comment in comments"
      :key="comment.id"
      :comment="comment"
      @reply="handleReply"
      @like="handleLike"
    />
  </div>
</template>

<script setup lang="ts">
import type { CommentType } from '../types';
import CommentCard from './CommentCard.vue';

defineProps<{
  comments: CommentType[];
}>();

const emit = defineEmits<{
  (e: 'reply', data: { commentId: number; content: string }): void;
  (e: 'like', commentId: number): void;
}>();

const handleReply = (data: { commentId: number; content: string }): void => {
  emit('reply', data);
};

const handleLike = (commentId: number): void => {
  emit('like', commentId);
};
</script>

<style scoped>
.comment-list {
  padding: 0;
  max-height: 800px;
  overflow-y: auto;
  overscroll-behavior: contain;
  scroll-behavior: smooth;
}

.comment-list::-webkit-scrollbar {
  width: 6px;
}

.comment-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.comment-list::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.comment-list::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}
</style> 