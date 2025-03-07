<template>
  <div class="question-content">
    <div class="question-text">{{ question.content }}</div>
    <div class="options-list">
      <div 
        v-for="option in question.options" 
        :key="option.label"
        class="option-item"
        :class="{ selected: selectedOptions.includes(option.label) }"
        @click="toggleOption(option.label)"
      >
        <span class="option-label">{{ option.label }}</span>
        <span class="option-text">{{ option.text }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps<{
  question: any;
}>();

const selectedOptions = ref<string[]>([]);

const toggleOption = (label: string) => {
  const index = selectedOptions.value.indexOf(label);
  if (index === -1) {
    selectedOptions.value.push(label);
  } else {
    selectedOptions.value.splice(index, 1);
  }
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
  flex-direction: column;
  gap: 12px;
}

.option-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px 16px;
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

.option-label {
  font-weight: 500;
  color: #666;
  min-width: 24px;
}

.option-text {
  flex: 1;
  line-height: 1.6;
}
</style>
