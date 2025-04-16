<template>
  <div class="sidebar">
    <div class="head">
      <h2 class="title">热门题目</h2>
    </div>
    <div class="hot-questions-container">
      <ul class="hot-questions-list">
        <li v-for="question in hotQuestions" :key="question.id" @click="handleQuestionClick(question.id)"
            class="question-item">
          <div class="question-title">{{ question.title }}</div>
          <div class="view-count">
            <img src="../../image/hot.png" alt="View" class="el-icon" />
            {{ question.view_count }}
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useQuestionBankStore } from '@/view/HomePage/view/StudyPage/store/QuestionBank';

const router = useRouter();
const questionBank = useQuestionBankStore();

// 获取所有题目并按照浏览次数降序排序，取前10个
const hotQuestions = computed(() => {
  return [...questionBank.questions]
      .sort((a, b) => b.view_count - a.view_count)
      .slice(0, 10);
});

// 处理点击题目事件，跳转到对应的题目详情页
const handleQuestionClick = (questionId: number) => {
  router.push({
    name: 'QuestionPage',
    params: {
      questionId: questionId.toString()
    }
  });
};
</script>

<style scoped>
.sidebar {
  width: 280px;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  margin: 0 auto;
}

.head {
  background: linear-gradient(180deg, #eeb587, #fff);
  border-radius: 8px 8px 0 0;
  padding: 0 18px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.hot-questions-container {
  flex: 1;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #e0e0e0 transparent;
}

.hot-questions-container::-webkit-scrollbar {
  width: 6px;
}

.hot-questions-container::-webkit-scrollbar-track {
  background: transparent;
}

.hot-questions-container::-webkit-scrollbar-thumb {
  background-color: #e0e0e0;
  border-radius: 3px;
}

.hot-questions-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.question-item {
  padding: 12px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s ease;
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.question-item:hover {
  background-color: #f5f7fa;
}

.question-title {
  font-size: 14px;
  color: #333;
  flex: 1;
  margin-right: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.view-count {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.view-count .el-icon {
  font-size: 14px;
}
</style>