<template>
  <div class="comment-form">
    <el-avatar :src="userAvatar" :size="40" class="user-avatar" />
    
    <div class="form-content">
      <el-input
        v-model="content"
        type="textarea"
        :rows="3"
        placeholder="分享你的观点和建议..."
        resize="none"
        :maxlength="1000"
        show-word-limit
      />
      
      <div class="form-footer">
        <el-button 
          type="primary" 
          :disabled="!content.trim()" 
          @click="submitComment"
        >
          发布评论
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emit = defineEmits<{
  (e: 'submit', content: string): void;
}>();

// 用于演示的用户头像
const userAvatar = ref<string>('https://placekitten.com/110/110');
const content = ref<string>('');

const submitComment = (): void => {
  if (content.value.trim()) {
    emit('submit', content.value);
    content.value = '';
  }
};
</script>

<style scoped>
.comment-form {
  padding: 24px 32px; /* 增大内边距与问题详情一致 */
  display: flex;
  gap: 16px; /* 增大间距 */
  border-bottom: 1px solid #f0f0f0;
}

.form-content {
  flex: 1;
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 12px; /* 增大间距 */
}

:deep(.el-textarea__inner) {
  border-radius: 8px;
  resize: none;
  padding: 16px; /* 增大文本域内边距 */
  line-height: 1.5;
  font-size: 14px;
}

:deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
</style> 