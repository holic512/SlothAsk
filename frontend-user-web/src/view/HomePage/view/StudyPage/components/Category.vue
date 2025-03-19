<script setup lang="ts">
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import {ArrowRight} from '@element-plus/icons-vue'
import {Category} from "@/view/HomePage/view/StudyPage/interface/CategoryInterface";
import {ApiGetRecommendCategory} from "@/view/HomePage/view/StudyPage/service/ApiGetRecommendCategory";

const router = useRouter();

// 定义存储推荐分类的list
const recommendCategoryList = ref<Category[]>([])
// 添加加载状态
const loading = ref<boolean>(true)
// 骨架屏显示的占位数量
const skeletonCount = ref<number>(6)

// 当页面渲染时 获取推荐的 分类list
onMounted(async () => {
  try {
    const response = await ApiGetRecommendCategory();
    if (response && response.status === 200) {
      recommendCategoryList.value = response.data;
    }
  } catch (error) {
    console.error('获取推荐分类失败:', error);
  } finally {
    loading.value = false;
  }
})

const handleViewMore = (): void => {
  router.push('/questionbank');
}

const handleCategoryClick = (categoryId: number): void => {
  router.push({
    name: 'CategoryDetail',
    params: {id: categoryId}
  });
}

</script>
<template>
  <div class="category-container">
    <div class="header">
      <h2 class="title">题库推荐</h2>
      <button class="view-more" @click="handleViewMore">
        查看更多
        <el-icon class="view-more-icon">
          <ArrowRight/>
        </el-icon>
      </button>
    </div>

    <div class="category-list">
      <!-- 加载中显示骨架屏 -->
      <template v-if="loading">
        <div class="category-item skeleton" v-for="i in skeletonCount" :key="`skeleton-${i}`">
          <div class="skeleton-img"></div>
          <div class="skeleton-content">
            <div class="skeleton-title"></div>
            <div class="skeleton-desc"></div>
          </div>
        </div>
      </template>
      
      <!-- 数据加载完成后显示实际内容 -->
      <template v-else>
        <div class="category-item" v-for="item in recommendCategoryList" :key="item.id"
             @click="handleCategoryClick(item.id)">
          <div class="icon-wrapper">
            <img class="recommend-category-icon" :src="item.avatarUrl">
          </div>
          <div class="content">
            <h3 class="category-item-title">{{ item.name }}</h3>
            <p class="count">{{ item.description }}</p>
          </div>
        </div>
        
        <!-- 如果没有数据，显示空状态 -->
        <div class="empty-state" v-if="recommendCategoryList.length === 0">
          <p>暂无推荐分类</p>
        </div>
      </template>
    </div>
  </div>
</template>


<style scoped>
.category-container {
  position: relative;
  overflow: hidden;
  width: 100%;
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
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  width: 100%;
  padding-bottom: 4px;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border-radius: 8px;
  background: white;
  border: 1px solid #eee;
  transition: all 0.1s ease;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06),
  0 2px 4px rgba(0, 0, 0, 0.03);
}

.category-item:hover {
  transform: translateY(-2px);
}

.icon-wrapper {
  margin-right: 16px;
}

.recommend-category-icon{
  height: 72px;
  width: 72px;
  border-radius: 4px;
}

.content {
  flex: 1;
  min-width: 0;
}

.category-item-title {
  font-size: 16px;
  font-weight: 500;
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
  white-space:nowrap;
  overflow: hidden;
}

/* 骨架屏样式 */
.skeleton {
  pointer-events: none;
}

.skeleton-img {
  width: 72px;
  height: 72px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  margin-right: 16px;
}

.skeleton-content {
  flex: 1;
}

.skeleton-title {
  height: 20px;
  margin-bottom: 8px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  border-radius: 4px;
  width: 70%;
}

.skeleton-desc {
  height: 16px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  border-radius: 4px;
  width: 90%;
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

/* 空状态样式 */
.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 32px;
  color: #999;
  font-size: 14px;
}

@media (max-width: 768px) {
  .category-list {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }

  .category-item {
    padding: 16px;
  }

  .icon-wrapper {
    width: 48px;
    height: 48px;
  }
}
</style>
