<template>
  <div class="category-page">
    <div class="carousel-container">
      <el-button
          class="carousel-button prev"
          @click="prevSlide"
          :disabled="currentSlide === 0"
      >
        <el-icon>
          <ArrowLeft/>
        </el-icon>
      </el-button>

      <div class="carousel" ref="carouselRef">
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
                  :span="Math.floor(24 / columnsPerRow)"
              >
                <el-card shadow="hover" class="category-card" @click="goToDetail(category.id)">
                  <div class="card-content">
                    <div class="image-text">
                      <div class="image-container">
                        <el-image :src="category.avatarUrl" class="category-image"/>
                        <div class="view-count">
                          <el-icon>
                            <View/>
                          </el-icon>
                          <span>{{ category.viewCount }}</span>
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
        <el-icon>
          <ArrowRight/>
        </el-icon>
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

<script lang="ts" setup>
import {ref, computed, onMounted, onUnmounted, watch, nextTick} from 'vue';
import {useRouter} from 'vue-router';
import {ArrowLeft, ArrowRight, View} from '@element-plus/icons-vue';
import {useCategoryStore} from "../store/CategoryStore"

const router = useRouter();
const props = defineProps({
  projectId: Number,
});
const categoryStore = useCategoryStore();
const currentSlide = ref(0);

const carouselRef = ref<HTMLElement | null>(null);
const containerWidth = ref(window.innerWidth);

// 更新容器宽度
const updateContainerWidth = () => {
  if (carouselRef.value) {
    containerWidth.value = carouselRef.value.clientWidth;
  }
};

//监听元素尺寸变化
let resizeObserver: ResizeObserver | null = null;

// 在容器尺寸变化时自动更新
onMounted(() => {
  nextTick(() => {
    updateContainerWidth();
    if (carouselRef.value) {
      resizeObserver = new ResizeObserver(() => {
        updateContainerWidth();
      });
      resizeObserver.observe(carouselRef.value);
    }
  });
});

onUnmounted(() => {
  if (resizeObserver && carouselRef.value) {
    resizeObserver.unobserve(carouselRef.value);
  }
});

// 每页展示数量
const itemsPerSlide = computed(() => {
  const width = containerWidth.value;
  if (width < 576) return 3;
  if (width < 992) return 6;
  return 9;
});

// 每行展示的列数（用于 el-col span 控制）
const columnsPerRow = computed(() => {
  const width = containerWidth.value;
  if (width < 576) return 1;
  if (width < 992) return 2;
  return 3;
});

// 当前页在尺寸变化时定位到正确位置
watch(itemsPerSlide, (newPerSlide, oldPerSlide) => {
  if (categoryStore.categories.length > 0 && oldPerSlide > 0) {
    const currentIndex = currentSlide.value * oldPerSlide;
    currentSlide.value = Math.floor(currentIndex / newPerSlide);
  }
});

const slides = computed(() => {
  const result = [];
  const categories = [...categoryStore.categories];
  const perSlide = itemsPerSlide.value;
  while (categories.length) {
    result.push(categories.splice(0, perSlide));
  }
  return result;
});

watch(() => props.projectId, (newProjectId) => {
  if (newProjectId) {
    categoryStore.fetchCategoriesByProjectIdPaged(newProjectId, 1, itemsPerSlide.value * 2);
    currentSlide.value = 0;
  }
}, {immediate: true});

const loadMoreCategories = () => {
  if (
      props.projectId &&
      slides.value.length > 0 &&
      currentSlide.value === slides.value.length - 1 &&
      categoryStore.categories.length < categoryStore.totalCategories
  ) {
    categoryStore.loadMoreCategories(props.projectId);
  }
};

watch(currentSlide, (newValue) => {
  if (newValue === slides.value.length - 1) {
    loadMoreCategories();
  }
});

const goToDetail = (id: number) => {
  router.push({name: 'CategoryDetail', params: {id: id.toString()}});
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

const goToSlide = (index: number) => {
  currentSlide.value = index;
};
</script>


<style scoped>
.category-page {
  width: 100%;
  padding: 1rem 0;
  position: relative;
  overflow: hidden;
}

.carousel-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.25rem;
  max-width: 100%;
  box-sizing: border-box;
}

.carousel {
  width: calc(100% - 5rem);
  overflow: hidden;
}

.carousel-inner {
  display: flex;
  transition: transform 0.5s ease;
  width: 100%;
}

.carousel-slide {
  flex: 0 0 100%;
  box-sizing: border-box;
  width: 100%;
}

.carousel-button {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;
  cursor: pointer;
  z-index: 1;
  flex-shrink: 0;
}

.carousel-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.category-row {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -0.5rem;
  width: 100%;
  box-sizing: border-box;
}

.category-card {
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border-radius: 8px;
  height: auto;
  min-height: 6rem;
  margin: 0.5rem;
  display: flex;
  align-items: center;
  padding: 0.5rem;
  background: white;
  border: 1px solid #eee;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  width: calc(100% - 1rem);
  box-sizing: border-box;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}

.card-content {
  display: flex;
  justify-content: flex-start;
  height: 100%;
  width: 100%;
}

.image-text {
  display: flex;
  width: 100%;
}

.image-container {
  width: 5.5rem;
  height: 5.5rem;
  overflow: hidden;
  border-radius: 8px;
  position: relative;
  margin-right: 0.75rem;
  flex-shrink: 0;
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
  min-width: 0; /* 防止文本溢出 */
}

.category-name {
  font-size: 1rem;
  font-weight: bold;
  margin: 0 0 0.5rem 0;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.category-desc {
  font-size: 0.75rem;
  color: #666;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
  height: 2.1rem; /* 2行文字的高度 */
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 1.25rem;
}

.pagination-dot {
  width: 0.625rem;
  height: 0.625rem;
  border-radius: 50%;
  background-color: #dcdfe6;
  margin: 0 0.25rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination-dot.active {
  background-color: #409eff;
}

.pagination-dot:hover {
  background-color: #c0c4cc;
}

@media (max-width: 992px) {
  .image-container {
    width: 4rem;
    height: 4rem;
  }

  .category-name {
    font-size: 0.875rem;
  }

  .category-desc {
    -webkit-line-clamp: 2;
    font-size: 0.7rem;
    height: 1.96rem; /* 2行文字的高度适配较小的字体 */
  }

}

@media (max-width: 768px) {
  .carousel {
    width: calc(100% - 4.5rem);
  }

  .carousel-button {
    width: 2rem;
    height: 2rem;
  }

  .category-card{
    padding: 4px;
  }
}

@media (max-width: 576px) {

  .image-text {
    flex-direction: column;
  }

  .image-container {
    width: 100%;
    height: 7rem;
    margin-right: 0;
    margin-bottom: 0.5rem;
  }

  .category-card {
    padding: 0.5rem;

  }

  .pagination-dot {
    width: 0.5rem;
    height: 0.5rem;
  }

  .category-desc {
    -webkit-line-clamp: 2;
    height: auto; /* 在小屏幕上自适应高度 */
    max-height: 2.1rem; /* 限制最大高度 */
  }
}
</style>