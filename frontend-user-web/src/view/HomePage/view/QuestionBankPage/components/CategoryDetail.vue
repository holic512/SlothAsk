<template>
  <div class="category-detail">
    <div class="category-header">
      <div class="category-info">
        <el-image class="category-icon" :src="category?.avatarUrl"/>
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
</template>

<script setup lang="ts">
import QuestionList from "@/view/HomePage/view/QuestionBankPage/components/QuestionList.vue";
import { useCategoryStore} from "@/view/HomePage/view/QuestionBankPage/store/CategoryStore";
import { useQuestionStore } from "@/view/HomePage/view/QuestionBankPage/store/QuestionStore";
import { computed, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { setTitle } from "@/utils/title";

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

  if (category.value) {
    setTitle(`${category.value.name}`);
  }
});

// 当分类ID变化时重新获取题目数量
watch(categoryId, async (newId: number) => {
  await questionStore.fetchQuestionCount(newId);
});

// 当分类名称变化时更新标题
watch(() => category.value?.name, (newTitle) => {
  if (newTitle) {
    setTitle(newTitle);
  }
});
</script>

<style scoped>
.category-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28px 32px;
  background: linear-gradient(to right, #ffffff, #f7f9fc);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  margin-bottom: 28px;
  transition: all 0.3s ease;
  border: 1px solid #eaeef5;
}

.category-header:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.category-info {
  display: flex;
  align-items: center;
  gap: 28px;
}

.category-icon {
  width: 80px;
  height: 80px;
  padding: 16px;
  background: linear-gradient(135deg, #f6f8fc, #eef2f7);
  border-radius: 20px;
  object-fit: contain;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  border: 1px solid #eaeef5;
  transition: all 0.3s ease;
}

.category-icon:hover {
  transform: scale(1.05);
}

.title-section {
  flex: 1;
}

.category-name {
  font-size: 30px;
  font-weight: 700;
  margin: 0 0 12px 0;
  color: #1a1a1a;
  letter-spacing: -0.5px;
}

.category-description {
  color: #5a6a85;
  margin: 0;
  font-size: 16px;
  line-height: 1.6;
  max-width: 80%;
}

.category-stats {
  display: flex;
  gap: 32px;
  background: rgba(255, 255, 255, 0.8);
  padding: 16px 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid #eaeef5;
}

.stat-item {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-value {
  display: block;
  font-size: 34px;
  font-weight: 700;
  color: black;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #5a6a85;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.question-list {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  padding: 28px;
  border: 1px solid #eaeef5;
  transition: all 0.3s ease;
}

.question-list:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
}


@media (max-width: 768px) {
  .category-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    padding: 24px;
  }

  .category-stats {
    width: 100%;
    justify-content: center;
  }

  .category-name {
    font-size: 26px;
  }

  .category-description {
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .category-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .category-icon {
    width: 64px;
    height: 64px;
    padding: 12px;
  }
}
</style>