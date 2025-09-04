<script lang="ts" setup>
import {defineProps} from 'vue';
import {FavQuestionItemInterface} from "@/view/HomePage/view/UserProfile/interface/FavQuestionItemInterface";
import {ElPagination} from 'element-plus';
import {useRouter} from "vue-router";
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')
dayjs.extend(relativeTime);

const router = useRouter();



const props = defineProps<{
  // 收藏列表数据，留空等待接口调用
  collections: FavQuestionItemInterface[];
  // 是否正在加载中
  isLoading: boolean;
  // 分页相关
  total: number;
  pageSize: number;
  currentPage: number;
}>();

const emit = defineEmits(['page-change']);

const handlePageChange = (page: number) => {
  emit('page-change', page);
};

const handleToQuestion = (questionId: string) => {
  router.push("/question/" + questionId);
};

const formatTime = (time: string) => {
  const now = dayjs();
  const target = dayjs(time);
  const diffDays = now.diff(target, 'day');
  const diffHours = now.diff(target, 'hour');
  const diffMinutes = now.diff(target, 'minute');
  
  if (diffDays >= 7) {
    return target.format('YYYY-MM-DD');
  } else if (diffDays >= 1) {
    return target.format('MM-DD HH:mm');
  } else if (diffHours >= 1) {
    return `${diffHours}小时前`;
  } else if (diffMinutes >= 1) {
    return `${diffMinutes}分钟前`;
  } else {
    return '刚刚';
  }
};
</script>

<template>
  <div class="collections-module">
    <div v-if="isLoading" class="empty-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="collections.length === 0" class="empty-state">
      <div class="empty-icon">
        <svg fill="none" height="64" viewBox="0 0 24 24" width="64" xmlns="http://www.w3.org/2000/svg">
          <path d="M17 3H7C5.9 3 5 3.9 5 5V21L12 18L19 21V5C19 3.9 18.1 3 17 3ZM17 19L12 16.82L7 19V5H17V19Z" fill="#90a4ae"/>
        </svg>
      </div>
      <p>暂无收藏内容</p>
    </div>

    <div v-else class="collections-list">
      <div v-for="collection in collections" :key="collection.questionId" class="collection-item">
        <div class="collection-main">
          <div class="collection-title-row">
            <el-text class="collection-title" @click="handleToQuestion(collection.questionId)">{{ collection.questionTitle }}</el-text>
            <el-text class="meta-time">{{ formatTime(collection.savedAt) }}</el-text>
          </div>
          <div class="meta-row">
            <div v-if="collection.tags && collection.tags.length > 0" class="collection-tags">
              <span v-for="tag in collection.tags" :key="tag" class="tag">{{ tag }}</span>
            </div>
            <el-text class="meta-views">访问量：{{ collection.views }}</el-text>
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="isLoading" class="collections-list">
      <div v-for="i in pageSize" :key="i" class="collection-item skeleton">
        <div class="collection-main">
          <div class="collection-title-row">
            <el-text class="collection-title skeleton-block"></el-text>
            <el-text class="meta-time skeleton-block" style="width: 60px;"></el-text>
          </div>
          <div class="meta-row">
            <el-text class="meta-views skeleton-block" style="width: 80px;"></el-text>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页组件 -->
    <el-pagination
        v-if="!isLoading && total > pageSize"
        :current-page="currentPage"
        :hide-on-single-page="true"
        :page-size="pageSize"
        :total="total"
        background
        class="collections-pagination"
        layout="prev, pager, next"
        @current-change="handlePageChange"
    />
  </div>
</template>

<style scoped>
.collections-module {
  min-height: 400px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #999;
  padding: 20px;
  text-align: center;
  background: #fafafa;
  margin: 20px;
  border-radius: 8px;
  border: 1px dashed #e0e0e0;
}

.loading-spinner {
  width: 36px;
  height: 36px;
  border: 3px solid #f0f0f0;
  border-top: 3px solid #409eff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.empty-icon {
  margin-bottom: 16px;
  opacity: 0.5;
}

.collections-list {
  display: flex;
  flex-direction: column;
}

.collection-item {
  position: relative;
  padding: 20px 24px;
  background-color: #ffffff;
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f0f0;
}

.collection-item:last-child {
  border-bottom: none;
}

.collection-item:hover {
  background-color: #f8f9fa;
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.collection-main {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.collection-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.collection-title {
  color: #333;
  font-size: 15px;
  font-weight: 500;
  line-height: 1.6;
  flex: 1;
  word-break: break-word;
  cursor: pointer;
  transition: color 0.2s;
  max-width: 80%;
}

.collection-title:hover {
  color: #2962ff;
}

.meta-time {
  color: #999;
  font-size: 12px;
  white-space: nowrap;
  flex-shrink: 0;
  background: #f0f9ff;
  padding: 2px 8px;
  border-radius: 12px;
  border: 1px solid #e3f2fd;
}

.collection-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  flex: 1;
}

.tag {
  display: inline-block;
  background: #e8f5e8;
  color: #2e7d32;
  border-radius: 12px;
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.tag:hover {
  background: #c8e6c9;
}

.meta-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  min-height: 24px;
}

.meta-views {
  color: #757575;
  font-size: 13px;
}

.collection-item.skeleton {
  pointer-events: none;
  box-shadow: none;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
}

.skeleton-block {
  background: linear-gradient(90deg, #eeeeee 25%, #e0e0e0 50%, #eeeeee 75%);
  background-size: 200% 100%;
  border-radius: 4px;
  animation: skeleton-loading 1.2s ease-in-out infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

/* 分页样式 */
.collections-pagination {
  margin: 32px 0;
  display: flex;
  justify-content: center;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .collection-item {
    padding: 16px;
  }

  .collection-title {
    font-size: 15px;
  }
}
</style>