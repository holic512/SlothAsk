<template>
  <div class="question-content">
    <div class="question-text">{{ question.content }}</div>
    <el-input
      v-model="answer"
      type="textarea"
      :rows="6"
      placeholder="请输入你的答案..."
      resize="none"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps<{
  question: any;
}>();

const answer = ref('');
</script>

<style scoped>
.question-text {
  margin-bottom: 24px;
}
</style>