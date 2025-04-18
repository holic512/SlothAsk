<template>
  <div class="question-content">
    <div class="options-list">
      <div v-for="option in options" :key="option.label" class="option-item"
           :class="{ selected: selectedOption === option.label }" @click="selectOption(option.label)">
        <span class="option-label">{{ option.label }}</span>
        <span class="option-text">{{ option.text }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

const props = defineProps<{
  question: any;
}>();

const selectedOption = ref('');

// 解析题目内容并提取选项
const options = computed(() => {
  if (!props.question.content) return [];

  // 使用正则从 content 中提取选项 A、B、C、D 等
  const optionRegex = /([A-D])\.\s*(.*?)(?=\n|$)/g;
  const optionsArray: { label: string; text: string }[] = [];
  let match;

  // 提取所有选项
  while ((match = optionRegex.exec(props.question.content)) !== null) {
    optionsArray.push({
      label: match[1],  // 选项的标签（A、B、C、D）
      text: match[2]    // 选项的文本内容
    });
  }

  return optionsArray;
});

// 选择选项
const selectOption = (label: string) => {
  selectedOption.value = label;
};
</script>

<style scoped>
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

