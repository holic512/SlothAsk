<template>
  <div class="category-page">
    <div class="carousel-container">
      <el-button
          class="carousel-button prev"
          @click="prevSlide"
          :disabled="currentSlide === 0"
      >
        <el-icon><ArrowLeft /></el-icon>
      </el-button>

      <div class="carousel">
        <div
            class="carousel-inner"
            :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
        >
          <div
              v-for="(slide, index) in slides"
              :key="index"
              class="carousel-slide"
          >
            <el-row :gutter="20" class="category-row">
              <el-col
                  v-for="category in slide"
                  :key="category.id"
                  :xs="24"
                  :sm="12"
                  :md="8"
                  :lg="8"
              >
                <el-card shadow="hover" class="category-card" @click="goToDetail(category.id)">
                  <div class="card-content">
                    <div class="image-text">
                      <div class="image-container">
                        <el-image :src="category.avatarUrl" class="category-image" />
                        <div class="view-count">
                          <el-icon><View /></el-icon>
                          <span>{{category.viewCount}}</span>
                        </div>
                      </div>
                      <div class="text-content">
                        <h3 class="category-name">{{ category.name }}</h3>
                        <p class="category-desc">{{ category.description }}</p>
                      </div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>

      <el-button
          class="carousel-button next"
          @click="nextSlide"
          :disabled="currentSlide === slides.length - 1"
      >
        <el-icon><ArrowRight /></el-icon>
      </el-button>
    </div>

    <div class="pagination">
      <span
          v-for="(_, index) in slides"
          :key="index"
          class="pagination-dot"
          :class="{ active: currentSlide === index }"
          @click="goToSlide(index)"
      ></span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { View, ArrowLeft, ArrowRight } from '@element-plus/icons-vue';
import { useCategoryStore } from "../store/CategoryStore.ts"

const router = useRouter();
const props = defineProps({
  projectId: Number,
});
const categoryStore = useCategoryStore();
const currentSlide = ref(0);

// 将分类数据分成每9个一组的幻灯片
const slides = computed(() => {
  const result = [];
  const categories = [...categoryStore.categories];
  while (categories.length) {
    result.push(categories.splice(0, 9));
  }
  return result;
});

watch(() => props.projectId, (newProjectId) => {
  if (newProjectId) {
    categoryStore.fetchCategoriesByProjectId(newProjectId);
    currentSlide.value = 0;
  }
}, { immediate: true });

const goToDetail = (id) => {
  router.push({ name: 'CategoryDetail', params: { id } });
};

const prevSlide = () => {
  if (currentSlide.value > 0) {
    currentSlide.value--;
  }
};

const nextSlide = () => {
  if (currentSlide.value < slides.value.length - 1) {
    currentSlide.value++;
  }
};

const goToSlide = (index) => {
  currentSlide.value = index;
};
</script>

<style scoped>
.category-page {
  width: 100%;
  padding: 20px 0;
  position: relative;
}

.carousel-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.carousel {
  width: calc(100% - 80px);
  overflow: hidden;
}

.carousel-inner {
  display: flex;
  transition: transform 0.5s ease;
  width: 100%;
}

.carousel-slide {
  flex: 0 0 100%;
  padding: 0 10px;
  box-sizing: border-box;
}

.carousel-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;
  cursor: pointer;
  z-index: 1;
}

.carousel-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.category-row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10px;
}

.category-card {
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border-radius: 8px;
  height: 150px;
  margin: 10px;
  display: flex;
  align-items: center;
  padding: 12px;
  background: white;
  border: 1px solid #eee;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06),
  0 2px 4px rgba(0, 0, 0, 0.03);
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}

.card-content {
  display: flex;
  justify-content: flex-start;
  height: 100%;
}

.image-text {
  display: flex;
  width: 100%;
}

.image-container {
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 8px;
  position: relative;
  margin-right: 15px;
}

.view-count {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 3px 6px;
  border-radius: 10px;
  font-size: 10px;
  display: flex;
  align-items: center;
}

.category-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.category-card:hover .category-image {
  transform: scale(1.05);
}

.view-count .el-icon {
  margin-right: 3px;
}

.text-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.category-name {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 8px 0;
  color: #333;
}

.category-desc {
  font-size: 12px;
  color: #666;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #dcdfe6;
  margin: 0 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination-dot.active {
  background-color: #409eff;
}

.pagination-dot:hover {
  background-color: #c0c4cc;
}
</style>