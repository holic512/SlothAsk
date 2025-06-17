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
  if (now.diff(target, 'day') >= 1) {
    return target.format('YYYY-MM-DD HH:mm:ss');
  } else {
    return target.fromNow();
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
          <path d="M17 3H7C5.9 3 5 3.9 5 5V21L12 18L19 21V5C19 3.9 18.1 3 17 3ZM17 19L12 16.82L7 19V5H17V19Z" fill="#CCCCCC"/>
        </svg>
      </div>
      <p>暂无收藏内容</p>
      <button class="create-btn">添加收藏</button>
    </div>

    <div v-else class="collections-list">
      <div v-for="collection in collections" :key="collection.questionId" class="collection-item">
        <div class="collection-header">
          <h3 class="collection-title" @click="handleToQuestion(collection.questionId)">{{ collection.questionTitle }}</h3>
        </div>
        <div class="collection-tags">
          <span v-for="tag in collection.tags" :key="tag" class="tag">{{ tag }}</span>
        </div>
        <div class="collection-meta">
          <span class="views">访问量：{{ collection.views }}</span>
          <span class="saved-date">收藏于 {{ formatTime(collection.savedAt) }}</span>
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
  min-height: 300px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #999;
  background-color: #fafafa;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-icon {
  margin-bottom: 16px;
  opacity: 0.5;
}

.create-btn {
  margin-top: 16px;
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.create-btn:hover {
  background-color: #0056b3;
}

.collections-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.collection-item {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.collection-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.collection-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.collection-title {
  margin: 0;
  font-size: 18px;
  color: #333;
  cursor: pointer;
  transition: color 0.2s;
}

.collection-title:hover {
  color: #007bff;
}

.collection-tags {
  margin-bottom: 8px;
}

.tag {
  display: inline-block;
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 4px;
  padding: 2px 8px;
  font-size: 12px;
  margin-right: 6px;
}

.views {
  margin-right: 16px;
}

.collection-meta {
  color: #999;
  font-size: 14px;
}

/* 分页样式 */
.collections-pagination {
  margin: 32px 0;
  display: flex;
  justify-content: center;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .collection-header {
    flex-direction: column;
  }

  .views {
    margin-top: 4px;
    margin-bottom: 8px;
  }
}
</style>