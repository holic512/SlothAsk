<template>
  <div class="category-detail-container">
    <el-scrollbar height="100%">
      <div class="category-detail">
        <div class="category-header">
          <div class="category-info">
            <el-image :src="category?.avatarUrl" class="category-icon"/>
            <div class="title-section">
              <h2 class="category-name">{{ category?.name }}</h2>
              <p class="category-description">{{ category?.description }}</p>
            </div>
          </div>
          <div class="category-stats">
            <div class="stat-item">
              <span class="stat-value">{{ questionCount }}</span>
              <span class="stat-label">题目数量</span>
            </div>
          </div>
        </div>

        <div class="question-list">
          <QuestionList :selected-category="categoryId" />
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import QuestionList from "@/view/HomePage/view/QuestionBankPage/BankPage/components/QuestionList.vue";
import {useCategoryStore} from "@/view/HomePage/view/QuestionBankPage/BankPage/store/CategoryStore";
import {useQuestionStore} from "@/view/HomePage/view/QuestionBankPage/BankPage/store/QuestionStore";
import {computed, onMounted, watch} from 'vue';
import {useRoute} from 'vue-router';
import {setTitle} from "@/utils/title";

const route = useRoute();
const questionStore = useQuestionStore();
const categoryStore = useCategoryStore();

// 获取分类 ID
const categoryId = computed<number>(() => Number(route.params.id));
// 获取分类详情
const category = computed(() => categoryStore.category);
// 获取题目数量
const questionCount = computed(() => questionStore.questionCount);

// 在组件挂载时获取题目数量
onMounted(async () => {
  // 获取分类详情和题目数量
  await categoryStore.fetchCategoryById(categoryId.value);
  await questionStore.fetchQuestionCount(categoryId.value);

  // 在获取分类详情后直接设置标题，避免使用额外的watcher
  if (category.value) {
    setTitle(`${category.value.name}`);
  }
});

// 当分类ID变化时重新获取题目数量
watch(categoryId, async (newId: number) => {
  await questionStore.fetchQuestionCount(newId);
  
  // 在分类ID变化后，获取新的分类详情并更新标题
  await categoryStore.fetchCategoryById(newId);
  if (category.value) {
    setTitle(`${category.value.name}`);
  }
});
</script>

<style scoped>
.category-detail-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.category-detail {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
  box-sizing: border-box;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  margin-bottom: 1.5rem;
  transition: all 0.3s ease;
  border: 1px solid #eaeef5;
  box-sizing: border-box;
  width: 100%;
}

.category-header:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.category-info {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex: 1;
  min-width: 0;
}

.category-icon {
  width: 5rem;
  height: 5rem;
  background: linear-gradient(135deg, #f6f8fc, #eef2f7);
  border-radius: 1rem;
  object-fit: contain;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  border: 1px solid #eaeef5;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.category-icon:hover {
  transform: scale(1.05);
}

.title-section {
  flex: 1;
  min-width: 0;
}

.category-name {
  font-size: 1.75rem;
  font-weight: 700;
  margin: 0 0 0.75rem 0;
  color: #1a1a1a;
  letter-spacing: -0.5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.category-description {
  color: #5a6a85;
  margin: 0;
  font-size: 1rem;
  line-height: 1.6;
  word-break: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.category-stats {
  display: flex;
  gap: 1.5rem;
  background: rgba(255, 255, 255, 0.8);
  padding: 1rem;
  border-radius: 0.75rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid #eaeef5;
  flex-shrink: 0;
}

.stat-item {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.stat-value {
  display: block;
  font-size: 2rem;
  font-weight: 700;
  color: black;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  color: #5a6a85;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.question-list {
  background: #ffffff;
  border-radius: 1rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  padding: 1.5rem;
  border: 1px solid #eaeef5;
  transition: all 0.3s ease;
  box-sizing: border-box;
  width: 100%;
}

.question-list:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
}

@media (max-width: 992px) {
  .category-name {
    font-size: 1.5rem;
  }
  
  .stat-value {
    font-size: 1.75rem;
  }
}

@media (max-width: 768px) {
  .category-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1.25rem;
    padding: 1.25rem;
  }

  .category-stats {
    width: 100%;
    justify-content: center;
  }

  .category-name {
    font-size: 1.25rem;
  }
  
  .category-icon {
    width: 4rem;
    height: 4rem;
  }
  
  .stat-value {
    font-size: 1.5rem;
  }
  
  .question-list {
    padding: 1rem;
  }
}

@media (max-width: 576px) {
  .category-detail {
    padding: 0.75rem;
  }

  .category-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .category-icon {
    width: 3.5rem;
    height: 3.5rem;
  }
  
  .category-description {
    font-size: 0.875rem;
    -webkit-line-clamp: 3;
  }
  
  .stat-label {
    font-size: 0.75rem;
  }
}
</style>