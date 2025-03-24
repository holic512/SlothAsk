<template>
  <div class="sidebar">
    <div class="search-box">
      <el-input v-model="searchQuery" placeholder="搜索题目" class="search-input">
        <template #prefix>
          <el-icon>
            <Search />
          </el-icon>
        </template>
      </el-input>
    </div>
    <div class="question-list-container">
      <ul class="question-list">
        <li v-for="item in filteredQuestions" :key="item.id" @click="handleQuestionClick(item.id)"
            :class="{ 'active': item.id === currentQuestion?.id }" class="question-item">
          {{ item.title }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useQuestionBankStore } from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import { Search } from '@element-plus/icons-vue';

// 获取路由信息和题库的题目
const route = useRoute();
const router = useRouter();
const questionBank = useQuestionBankStore();
// 侧边栏的可见性
const drawerVisible = ref(false);


// 获取路由中的 categoryId 参数
const categoryId = computed(() => {
  return Number(route.params.categoryId) || 'all'; // 默认 'all' 如果没有 categoryId
});

const currentQuestion = computed(() => {
  return questionBank.getAllQuestions.find(q => q.id === Number(route.params.questionId));
});

// 搜索框的输入内容
const searchQuery = ref('');

// 获取与当前题目在同一题库中的所有题目
const relatedQuestions = computed(() => {
  return questionBank.getCategoryQuestions(categoryId.value);
});

// 根据搜索框筛选题目
const filteredQuestions = computed(() => {
  return relatedQuestions.value.filter((question) => {
    return question.title.toLowerCase().includes(searchQuery.value.toLowerCase());
  });
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
  width: 300px;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;

  padding: 20px;
}

.search-box {
  margin-bottom: 16px;
}





.question-list-container {
  flex: 1;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #e0e0e0 transparent;
}

.question-list-container::-webkit-scrollbar {
  width: 6px;
}

.question-list-container::-webkit-scrollbar-track {
  background: transparent;
}

.question-list-container::-webkit-scrollbar-thumb {
  background-color: #e0e0e0;
  border-radius: 3px;
}

.question-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.question-item {
  padding: 12px 16px;
  cursor: pointer;
  border-radius: 6px;
  font-size: 14px;
  color: #333;
  transition: all 0.2s ease;
  margin-bottom: 4px;
}

.question-item:hover {
  background-color: #f5f7fa;
}

.question-item.active {
  background-color: #e6f7ff;
  color: #1890ff;
  font-weight: 500;
}
</style>