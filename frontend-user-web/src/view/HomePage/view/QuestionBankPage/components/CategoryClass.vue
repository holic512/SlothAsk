<template>
  <div class="category-class">
    <div class="categories-grid">
      <div v-for="category in categories" :key="category.id" class="category-card">
        <div class="category-header">
          <div class="category-icon">{{ category.avatar_url }}</div>
          <h3 class="category-name">{{ category.name }}</h3>
        </div>

        <div class="category-info">
          <p class="category-description">{{ category.description }}</p>
          <div class="tags">
            <span v-for="tag in getTopTags(category)" :key="tag" class="tag">
              {{ tag }}
            </span>
          </div>
        </div>

        <div class="category-footer">
          <el-button type="primary" text @click="handleCategoryClick(category.id)">
            开始学习
            <el-icon class="el-icon--right">
              <ArrowRight />
            </el-icon>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useQuestionBankStore } from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import { ArrowRight } from '@element-plus/icons-vue';

const router = useRouter();
const questionBank = useQuestionBankStore();
const categories = computed(() => questionBank.getAllCategories);

const handleCategoryClick = (categoryId: number) => {
  router.push({
    name: 'CategoryDetail',
    params: { id: categoryId }
  });
};

const getTopTags = (category: any) => {
  if (!category || !category.questions) return [];

  const tagCount = new Map();
  category.questions.forEach(q => {
    if (q.tags) {
      const tags = typeof q.tags === 'string' ? q.tags.split(',').map(t => t.trim()) : q.tags;
      tags.forEach(tag => {
        if (tag) tagCount.set(tag, (tagCount.get(tag) || 0) + 1);
      });
    }
  });

  return Array.from(tagCount.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, 3)
    .map(entry => entry[0]);
};
</script>

<style scoped>
.category-class {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.category-card {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  border: 1px solid #eee;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  overflow: hidden;
  height: 100%;
  min-height: 220px;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.category-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(45deg, #f8f9fa, #ffffff);
  border-bottom: 1px solid #f0f0f0;
}

.category-icon {
  font-size: 32px;
  padding: 10px;
  background: white;
  border-radius: 10px;
  line-height: 1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.category-name {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.category-info {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-height: 100px;
}

.category-description {
  font-size: 13px;
  color: #666;
  margin: 0;
  line-height: 1.5;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag {
  padding: 3px 10px;
  background: #f5f7fa;
  border-radius: 16px;
  font-size: 12px;
  color: #666;
  transition: all 0.3s;
}

.tag:hover {
  background: #ecf5ff;
  color: #409eff;
}

.category-footer {
  padding: 12px 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  background: #fff;
}

@media (max-width: 768px) {
  .categories-grid {
    grid-template-columns: 1fr;
  }
  
  .category-header {
    padding: 14px;
  }
  
  .category-icon {
    font-size: 28px;
    padding: 8px;
  }
}
</style>
