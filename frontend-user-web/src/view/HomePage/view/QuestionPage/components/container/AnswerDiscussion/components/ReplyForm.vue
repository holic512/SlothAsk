<template>
  <div class="reply-form">
    <el-input
      v-model="replyContent"
      type="textarea"
      :rows="2"
      placeholder="写下你的回复..."
      :maxlength="500"
      show-word-limit
    />
    <div class="form-actions">
      <el-button text @click="handleCancel">取消</el-button>
      <el-button 
        type="primary" 
        size="small" 
        :disabled="!replyContent.trim()"
        @click="submitReply"
      >
        回复
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  commentId: number;
}>();

const emit = defineEmits<{
  (e: 'submit', content: string): void;
  (e: 'cancel'): void;
}>();

// 使用 defineModel 代替 v-model (Vue 3.5 新特性)
const replyContent = defineModel<string>({ default: '' });

const submitReply = (): void => {
  if (replyContent.value.trim()) {
    emit('submit', replyContent.value);
    replyContent.value = '';
  }
};

const handleCancel = (): void => {
  emit('cancel');
  replyContent.value = '';
};
</script>

<style scoped>
.reply-form {
  margin-top: 16px;
  margin-bottom: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
  gap: 8px;
}

:deep(.el-textarea__inner) {
  background-color: #fff;
  border-radius: 6px;
  padding: 12px;
}
</style> 