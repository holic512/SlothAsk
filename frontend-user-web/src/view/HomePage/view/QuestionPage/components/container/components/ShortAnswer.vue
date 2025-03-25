<template>
  <div class="short-answer-container">
    <!-- 安全渲染HTML内容 -->
    <div class="question-content" v-html="sanitizedContent"></div>
    
    <!-- 答题区域 -->
    <div class="answer-area">
      <el-input
        v-model="answer"
        type="textarea"
        :rows="8"
        placeholder="请输入你的答案..."
        resize="vertical"
        class="answer-input"
      />
      <div class="submit-actions">
        <el-button type="primary" @click="submitAnswer">提交答案</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed} from 'vue';
import DOMPurify from 'dompurify';

const props = defineProps({
  question: {
    type: Object,
    required: true
  }
});

const answer = ref('');

// 使用DOMPurify净化HTML内容，防止XSS攻击
const sanitizedContent = computed(() => {
  if (!props.question?.content) return '';
  return DOMPurify.sanitize(props.question.content);
});

// 提交答案方法
const submitAnswer = () => {
  console.log('提交答案:', answer.value);
  // TODO: 实现提交逻辑
};

// 暴露获取答案的方法供父组件调用
defineExpose({
  getAnswer: () => answer.value
});
</script>

<style scoped>
.short-answer-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 使v-html渲染的内容也能应用样式 */
:deep(.question-content) {
  color: #333;
  line-height: 1.7;
  font-size: 1.05rem;
  
  /* 段落样式 */
  p {
    margin-bottom: 1.2em;
  }
  
  /* 首字母样式 */
  p:first-of-type::first-letter {
    font-size: 1.5em;
    font-weight: 500;
    color: #3b82f6;
  }
  
  /* 列表样式 */
  ul, ol {
    padding-left: 1.5em;
    margin-bottom: 1.2em;
  }
  
  li {
    margin-bottom: 0.5em;
  }
  
  /* 代码块样式 */
  pre, code {
    background-color: #f5f7fa;
    border-radius: 4px;
    font-family: 'Fira Code', 'Consolas', monospace;
    padding: 0.2em 0.4em;
    font-size: 0.9em;
  }
  
  pre {
    padding: 1em;
    overflow-x: auto;
    margin: 1.2em 0;
  }
  
  /* 图片样式 */
  img {
    max-width: 100%;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }
  
  /* 引用样式 */
  blockquote {
    border-left: 4px solid #3b82f6;
    padding-left: 1em;
    margin-left: 0;
    color: #555;
    font-style: italic;
  }
  
  /* 标题样式 */
  h1, h2, h3, h4 {
    margin-top: 1.5em;
    margin-bottom: 0.8em;
    font-weight: 600;
    line-height: 1.3;
  }
}

.answer-area {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.answer-input {
  border-radius: 8px;
}

:deep(.el-textarea__inner) {
  padding: 16px;
  font-size: 1rem;
  line-height: 1.6;
  border-color: #e5e7eb;
  transition: border-color 0.2s, box-shadow 0.2s;
}

:deep(.el-textarea__inner:focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.submit-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 0.5rem;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .short-answer-container {
    gap: 1.5rem;
  }
  
  :deep(.question-content) {
    font-size: 1rem;
  }
}
</style>