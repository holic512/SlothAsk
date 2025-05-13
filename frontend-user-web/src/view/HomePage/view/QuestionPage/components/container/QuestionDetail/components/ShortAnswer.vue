<template>
  <div class="short-answer-container">
    <!-- 问题内容 -->
    <div class="question-content" v-html="sanitizedContent"></div>
    <!-- 分割线 -->
    <el-divider/>

    <!-- 答案区域 -->
    <div class="question-answer">
      <!-- 展示答案 -->
      <div v-if="showAnswer" class="answer-content">
        <div class="answer-actions-top">
          <h2 class="section-title">答案解析</h2>
          <a class="toggle-link" @click.prevent="toggleAnswer">
            <i class="el-icon-close"/> 隐藏答案
          </a>
        </div>
        <div class="answer-text" v-html="sanitizedAnswer"></div>
      </div>

      <!-- 毛玻璃占位 -->
      <div v-else class="answer-placeholder">
        <div class="placeholder-content">
          <div v-for="n in 5" :key="n" class="line"></div>
        </div>
        <a class="view-link" @click.prevent="isLoggedIn ? toggleAnswer() : showLoginTip()">
          <i v-if="isLoading" class="el-icon-loading spinning"/>
          <span v-if="!isLoading">{{ isLoggedIn ? '查看答案' : '登录后查看答案' }}</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watch} from 'vue';
import DOMPurify from 'dompurify';
import {
  ApiGetQuestionAnswerByVirtualId
} from '@/view/HomePage/view/QuestionPage/service/ApiGetQuestionAnswerByVirtualId';
import {useSessionStore} from "@/pinia/Session";
import {useRoute, useRouter} from 'vue-router';
import {ElMessage, ElMessageBox} from 'element-plus';

const router = useRouter();
const route = useRoute();
const userSession = useSessionStore();

// 判断用户是否登录
const isLoggedIn = computed(() => {
  return userSession.userSession && userSession.userSession.tokenValue;
});

// 防抖函数
const debounce = (fn: Function, delay: number) => {
  let timer: ReturnType<typeof setTimeout> | null = null;
  return function(this: any, ...args: any[]) {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      fn.apply(this, args);
      timer = null;
    }, delay);
  };
};

// 防抖状态标志
const isToggling = ref(false);

// 显示登录提示
const showLoginTip = () => {
  ElMessageBox.confirm('登录后才能查看答案，是否前往登录？', '提示', {
    confirmButtonText: '去登录',
    cancelButtonText: '取消',
    type: 'info'
  }).then(() => {
    handleLogin();
  }).catch(() => {
    // 用户取消登录
  });
};

// 处理登录跳转
const handleLogin = () => {
  router.push({
    path: '/sign/email',
    query: {
      redirect: route.fullPath
    }
  });
};

const props = defineProps({
  question: {
    type: Object as () => { content: string; answer: string | null; virtualId: string },
    required: true
  }
});

const showAnswer = ref(false);
const isLoading = ref(false);

// 从缓存获取答案
const getAnswerFromCache = (virtualId: string): string | null => {
  try {
    return localStorage.getItem(`answer_cache_${virtualId}`);
  } catch (e) {
    console.warn('无法访问本地存储', e);
    return null;
  }
};

// 将答案存入缓存
const saveAnswerToCache = (virtualId: string, answer: string): void => {
  try {
    localStorage.setItem(`answer_cache_${virtualId}`, answer);
  } catch (e) {
    console.warn('无法写入本地存储', e);
  }
};

// 初始化时检查缓存
onMounted(() => {
  const cachedAnswer = getAnswerFromCache(props.question.virtualId);
  if (cachedAnswer && !props.question.answer) {
    props.question.answer = cachedAnswer;
  }
});

watch(
    () => props.question,
    () => {
      showAnswer.value = false;
      isLoading.value = false;
      isToggling.value = false;
      
      // 当问题变化时，检查缓存中是否有答案
      const cachedAnswer = getAnswerFromCache(props.question.virtualId);
      if (cachedAnswer && !props.question.answer) {
        props.question.answer = cachedAnswer;
      }
    }
);

const sanitizedContent = computed(() => DOMPurify.sanitize(props.question.content || ''));
const sanitizedAnswer = computed(() => DOMPurify.sanitize(props.question.answer || ''));

// 不带防抖的切换答案函数
const toggleAnswerWithoutDebounce = async () => {
  // 如果正在处理中，直接返回
  if (isToggling.value) return;
  
  // 设置处理标志
  isToggling.value = true;
  
  if (!showAnswer.value) {
    // 如果未登录，不执行后续操作
    if (!isLoggedIn.value) {
      ElMessage.warning('登录后才能查看答案');
      isToggling.value = false;
      return;
    }
    
    // 先检查缓存
    const cachedAnswer = getAnswerFromCache(props.question.virtualId);
    if (cachedAnswer && !props.question.answer) {
      props.question.answer = cachedAnswer;
    }
    
    // 如果缓存没有，则请求API
    if (!props.question.answer) {
      isLoading.value = true;
      
      try {
        const res = await ApiGetQuestionAnswerByVirtualId(props.question.virtualId);
        if (res.status === 200) {
          props.question.answer = res.data;
          // 保存到缓存
          saveAnswerToCache(props.question.virtualId, res.data);
        }
      } catch (error) {
        console.error('加载答案失败', error);
        ElMessage.error('加载答案失败，请稍后重试');
      }
      
      isLoading.value = false;
    }
    
    showAnswer.value = true;
  } else {
    showAnswer.value = false;
  }
  
  // 重置处理标志
  isToggling.value = false;
};

// 使用防抖包装切换答案函数
const toggleAnswer = debounce(toggleAnswerWithoutDebounce, 100);
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
  align-items: center; /* 垂直居中 */
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

:deep(.answer-text) {
  font-size: 1.05rem;
  line-height: 1.8;
  color: #2c3e50;
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
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
