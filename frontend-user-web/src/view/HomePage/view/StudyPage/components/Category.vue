<template>
  <div class="category-container">
    <div class="header">
      <h2 class="title">题库推荐</h2>
      <button class="view-more" @click="handleViewMore">
        查看更多
        <el-icon class="view-more-icon">
          <ArrowRight />
        </el-icon>
      </button>
    </div>

    <div class="category-list">
      <div class="category-item" v-for="item in displayedCategories" :key="item.id"
        @click="handleCategoryClick(item.id)">
        <div class="icon-wrapper">
          <img class="category-icon" :src="item.avatar_url" alt="Category Icon">
        </div>
        <div class="content">
          <h3 class="category-item-title">{{ item.name }}</h3>
          <p class="count">{{ getQuestionCount(item.id) }}个问题</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, watchEffect, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useQuestionBankStore } from '@/view/HomePage/view/store/QuestionBank';
import { ArrowRight } from '@element-plus/icons-vue'
const router = useRouter();
const questionBank = useQuestionBankStore();

// 使用Map存储每个分类的问题数量
const categoryQuestionCounts = ref(new Map<number, number>());

// 先定义computed属性
const displayedCategories = computed(() => questionBank.getDisplayCategories);

// 监听分类变化并更新问题数量
watchEffect(() => {
  displayedCategories.value?.forEach(category => {
    categoryQuestionCounts.value.set(
      category.id,
      questionBank.getQuestionCountByCategory(category.id)
    );
  });
});

const getQuestionCount = (categoryId: number): number => {
  return categoryQuestionCounts.value.get(categoryId) || 0;
};

const handleViewMore = (): void => {
  router.push('/questionbank');
}

const handleCategoryClick = (categoryId: number): void => {
  router.push({
    name: 'CategoryDetail',
    params: { id: categoryId }
  });
}
</script>

<style scoped>
.category-container {
  position: relative;
  overflow: hidden;
}

/* 确保内容在渐变层之上 */
.header,
.category-list {
  position: relative;
  z-index: 1;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.title {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #1a1a1a;
  position: relative;
  padding-left: 16px;
}

.title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background: #333;
  border-radius: 2px;
}

.view-more {
  border: none;
  background: none;
  color: #1890ff;
  cursor: pointer;
  font-size: 14px;
  padding: 8px 16px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
  border-radius: 20px;
}

.view-more-icon {
  font-size: 14px;
  transition: transform 0.3s ease;
}

.view-more:hover .view-more-icon {
  transform: translateX(4px);
}

.category-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 12px;
  background: white;
  border: 1px solid #eee;
  transition: all 0.3s ease;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.category-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.icon-wrapper {
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 16px;
  background: #f8f9fa;
  margin-right: 16px;
}

.icon {
  font-size: 28px;
}

.content {
  flex: 1;
  min-width: 0;
}

.category-item-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.count {
  font-size: 14px;
  color: #666;
  margin: 0;
  opacity: 0.8;
}
</style>
