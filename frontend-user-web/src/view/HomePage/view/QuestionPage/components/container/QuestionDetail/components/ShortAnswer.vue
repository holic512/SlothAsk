<template>
  <div class="short-answer-container">
    <!-- 问题内容 -->
    <div class="question-content" v-html="sanitizedContent"></div>
    <!-- 分割线 -->
    <el-divider />

    <!-- 答案区域 -->
    <div class="question-answer">
      <!-- 展示答案 -->
      <div v-if="showAnswer" class="answer-content">
        <div class="answer-actions-top">
          <h2 class="section-title">答案解析</h2>
          <a class="toggle-link" @click.prevent="toggleAnswer">
            <i class="el-icon-close" /> 隐藏答案
          </a>
        </div>
        <div class="answer-text" v-html="sanitizedAnswer"></div>
      </div>

      <!-- 毛玻璃占位 -->
      <div v-else class="answer-placeholder">
        <div class="placeholder-content">
          <div v-for="n in 5" :key="n" class="line"></div>
        </div>
        <a class="view-link" @click.prevent="toggleAnswer">
          <i v-if="isLoading" class="el-icon-loading spinning" />
          <span v-if="!isLoading">查看答案</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, ref, watch} from 'vue';
import DOMPurify from 'dompurify';
import {
  ApiGetQuestionAnswerByVirtualId
} from '@/view/HomePage/view/QuestionPage/service/ApiGetQuestionAnswerByVirtualId';

const props = defineProps({
  question: {
    type: Object as () => { content: string; answer: string | null; virtualId: string },
    required: true
  }
});

const showAnswer = ref(false);
const isLoading = ref(false);

watch(
    () => props.question,
    () => { showAnswer.value = false; isLoading.value = false; }
);

const sanitizedContent = computed(() => DOMPurify.sanitize(props.question.content || ''));
const sanitizedAnswer = computed(() => DOMPurify.sanitize(props.question.answer || ''));

const toggleAnswer = async () => {
  if (!showAnswer.value) {
    isLoading.value = true;
    if (!props.question.answer) {
      const res = await ApiGetQuestionAnswerByVirtualId(props.question.virtualId);
      if (res.status === 200) props.question.answer = res.data;
    }
    isLoading.value = false;
    showAnswer.value = true;
  } else {
    showAnswer.value = false;
  }
};
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

/* 答案区域 */
.question-answer {
  position: relative;
}

.answer-actions-top {
  display: flex;
  justify-content: space-between; /* 左右对齐 */
  align-items: center;            /* 垂直居中 */
}

.toggle-link {
  color: #e74c3c;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
}
.toggle-link:hover {
  text-decoration: underline;
}
.answer-text {
  font-size: 1.05rem;
  line-height: 1.8;
  color: #2c3e50;
}

/* 占位毛玻璃效果 */
.answer-placeholder {
  position: relative;
  padding: 1rem 0;
}
.placeholder-content {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
  filter: blur(6px);
}
.placeholder-content .line {
  height: 1em;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}
.view-link {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.4rem 0.8rem;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 4px;
  color: #3498db;
  font-weight: 600;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.2s;
}
.view-link:hover {
  background: rgba(255, 255, 255, 1);
}
.spinning {
  font-size: 1.2em;
  animation: spin 1s linear infinite;
}
@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
