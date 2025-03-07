<template>
  <div class="question-content">
    <div class="question-text">{{ question.content }}</div>
    <div class="options-list">
      <div 
        v-for="option in options" 
        :key="option.id"
        class="option-item"
        :class="{ selected: selectedAnswer === option.id }"
        @click="selectAnswer(option.id)"
      >
        {{ option.label }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps<{
  question: any;
}>();

const selectedAnswer = ref<string | null>(null);

interface Option {
  id: string;
  label: string;
  value: boolean;
}

const options: Option[] = [
  { id: 'true', label: '正确', value: true },
  { id: 'false', label: '错误', value: false }
];

const selectAnswer = (id: string) => {
  selectedAnswer.value = id;
};
</script>

<style scoped>
.question-text {
  font-size: 15px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 24px;
}

.options-list {
  display: flex;
  gap: 16px;
  margin-top: 24px;
}

.option-item {
  flex: 1;
  text-align: center;
  padding: 12px 24px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.option-item:hover {
  background: #f5f7fa;
}

.option-item.selected {
  background: #e6f7ff;
  border-color: #91d5ff;
}
</style>
