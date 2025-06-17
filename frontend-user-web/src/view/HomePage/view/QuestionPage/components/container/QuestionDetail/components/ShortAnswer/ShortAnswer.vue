<template>
  <div class="short-answer-container">
    <!-- 问题内容 -->
    <div class="question-content" v-html="sanitizedContent"></div>


    <!-- 标签页区域 -->
    <el-tabs v-model="activeTab" class="answer-tabs">
      <el-tab-pane label="问题答案" name="question-answer">
        <QuestionAnswer :question="question" />
      </el-tab-pane>
      <el-tab-pane label="我的回答" name="my-answer">
        <MyAnswer :question="question" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue'
import DOMPurify from 'dompurify'
import QuestionAnswer from './components/QuestionAnswer.vue'
import MyAnswer from './components/MyAnswer.vue'

const props = defineProps<{
  question: {
    content: string
    answer: string | null
    virtualId: string
  }
}>()

// 当前激活的标签页
const activeTab = ref('question-answer')

// 问题内容的安全化处理
const sanitizedContent = computed(() => DOMPurify.sanitize(props.question.content || ''))
</script>

<style scoped>
.short-answer-container {
  display: flex;
  flex-direction: column;
  font-family: 'Inter', sans-serif;
  color: #2c3e50;
}

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

/* 标签页样式 */
.answer-tabs {
  margin-top: 1rem;
}

:deep(.el-tabs__header) {
  margin-bottom: 1rem;
}

:deep(.el-tabs__nav-wrap) {
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-tabs__item) {
  font-weight: 500;
  color: #606266;
  transition: all 0.3s;
}

:deep(.el-tabs__item:hover) {
  color: #409eff;
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
  font-weight: 600;
}

:deep(.el-tabs__active-bar) {
  background-color: #409eff;
}
</style>
