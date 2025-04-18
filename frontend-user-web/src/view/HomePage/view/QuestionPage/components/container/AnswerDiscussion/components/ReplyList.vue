<template>
  <div class="reply-list">
    <reply-card
      v-for="reply in visibleReplies"
      :key="reply.id"
      :reply="reply"
    />
    
    <div v-if="hasMoreReplies" class="show-more">
      <el-button 
        link 
        type="primary" 
        @click="showMoreReplies"
      >
        {{ isShowingAll ? '收起回复' : `显示更多回复 (${remainingCount})` }}
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import ReplyCard from './ReplyCard.vue';
import type { ReplyType } from '../types';

const props = defineProps<{
  replies: ReplyType[];
}>();

const defaultShown = 2; // 默认显示的回复数量
const isShowingAll = ref<boolean>(false);

const visibleReplies = computed<ReplyType[]>(() => {
  if (isShowingAll.value || props.replies.length <= defaultShown) {
    return props.replies;
  }
  return props.replies.slice(0, defaultShown);
});

const remainingCount = computed<number>(() => {
  return props.replies.length - defaultShown;
});

const hasMoreReplies = computed<boolean>(() => {
  return props.replies.length > defaultShown;
});

const showMoreReplies = (): void => {
  isShowingAll.value = !isShowingAll.value;
};
</script>

<style scoped>
.reply-list {
  margin-top: 16px; /* 增大间距 */
  padding-left: 16px; /* 增大内边距 */
  border-left: 2px solid #eee;
}

.show-more {
  padding: 12px 0; /* 增大内边距 */
  display: flex;
  justify-content: center;
}
</style> 