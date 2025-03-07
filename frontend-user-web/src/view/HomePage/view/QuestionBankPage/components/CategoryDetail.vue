<template>
  <div class="category-detail">
    <div class="category-header">
      <div class="category-info">
        <div class="category-icon">{{ category?.icon }}</div>
        <div class="title-section">
          <h2 class="category-name">{{ category?.name }}</h2>
          <p class="category-description">{{ category?.description }}</p>
        </div>
      </div>
      <div class="category-stats">
        <div class="stat-item">
          <span class="stat-value">{{ category?.questions.length }}</span>
          <span class="stat-label">题目数量</span>
        </div>
      </div>
    </div>

    <div class="question-list">
      <Question 
        :selected-category="categoryId"
        :show-category-select="false"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useQuestionBankStore } from '../../pinia/QuestionBank';
import Question from '../../StudyPage/components/Question.vue';

const route = useRoute();
const questionBank = useQuestionBankStore();

const categoryId = computed(() => Number(route.params.id));

const category = computed(() => {
  return questionBank.categories.find(c => c.id === categoryId.value);
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
  padding: 32px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 24px;
}

.category-info {
  display: flex;
  align-items: center;
  gap: 24px;
}

.category-icon {
  font-size: 64px;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 16px;
  line-height: 1;
}

.title-section {
  flex: 1;
}

.category-name {
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 12px 0;
}

.category-description {
  color: #666;
  margin: 0;
  font-size: 16px;
  line-height: 1.6;
}

.category-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 32px;
  font-weight: 600;
  color: #1a1a1a;
}

.stat-label {
  font-size: 16px;
  color: #666;
  margin-top: 4px;
}

.question-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 24px;
}
</style> 