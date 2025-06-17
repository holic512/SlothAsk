<script lang="ts" setup>
import {defineEmits, defineProps} from 'vue';
import {CommentItemInterface} from "@/view/HomePage/view/UserProfile/interface/CommentItemInterface";
import {useRoute, useRouter} from "vue-router";
import {ElPagination} from 'element-plus';
import {Star} from '@element-plus/icons-vue';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')

dayjs.extend(relativeTime);

const router = useRouter();
const route = useRoute();

const props = defineProps<{
  answers: CommentItemInterface[];
  isLoading: boolean;
  total: number;
  pageSize: number;
  currentPage: number;
}>();

const emit = defineEmits(['page-change']);

const handleToQuestion = (questionTUid: string) => {
  router.push("/question/" + questionTUid)
}

const handlePageChange = (page: number) => {
  emit('page-change', page);
}

const formatTime = (time: string) => {
  const now = dayjs();
  const target = dayjs(time);
  if (now.diff(target, 'day') >= 1) {
    return target.format('YYYY-MM-DD HH:mm:ss');
  } else {
    return target.fromNow();
  }
}

</script>

<template>
  <div class="answers-module">
    <div v-if="isLoading" class="empty-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="answers.length === 0" class="empty-state">
      <div class="empty-icon">
        <svg fill="none" height="64" viewBox="0 0 24 24" width="64" xmlns="http://www.w3.org/2000/svg">
          <path
              d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20ZM13 7H11V13H17V11H13V7Z"
              fill="#7E57C2"/>
        </svg>
      </div>
      <p>暂无回答内容</p>
    </div>

    <div v-else class="answers-list">
      <div v-for="answer in answers" :key="answer.questionTUid" class="answer-item">
        <div class="answer-main">
          <div class="answer-content-time-row">
            <el-text class="answer-content">{{ answer.content }}</el-text>
            <el-text class="meta-time">{{ formatTime(answer.createdAt) }}</el-text>
          </div>
          <div class="meta-row meta-row-bottom">
            <el-text class="meta-source">
              来源:
              <el-link class="source-title" type="primary" @click="handleToQuestion(answer.questionTUid)">
                {{ answer.questionTitle }}
              </el-link>
            </el-text>
            <div class="answer-like">
              <el-icon :size="16" style="vertical-align: middle;">
                <Star/>
              </el-icon>
              <el-text class="like-num">{{ answer.likes }}</el-text>
            </div>
          </div>
        </div>
      </div>

    </div>
    <div v-if="isLoading" class="answers-list">
      <div v-for="i in pageSize" :key="i" class="answer-item skeleton">
        <div class="answer-main">
          <div class="answer-content-time-row">
            <el-text class="answer-content skeleton-block"></el-text>
            <el-text class="meta-time skeleton-block" style="width: 60px;"></el-text>
          </div>
          <div class="meta-row meta-row-bottom">
            <el-text class="meta-source skeleton-block" style="width: 120px;"></el-text>
            <el-text class="meta-like skeleton-block" style="width: 40px;"></el-text>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
        v-if="!isLoading && total > pageSize"
        :current-page="currentPage"
        :hide-on-single-page="true"
        :page-size="pageSize"
        :total="total"
        background
        class="answers-pagination"
        layout="prev, pager, next"
        @current-change="handlePageChange"
    />
  </div>
</template>

<style scoped>
.answers-module {
  min-height: 400px;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Segoe UI', 'Microsoft YaHei', sans-serif;
  background-color: #f9fafb;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #90a4ae;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  border: 1px dashed #cfd8dc;
  background: #ffffff;
}

.loading-spinner {
  width: 36px;
  height: 36px;
  border: 3px solid #eceff1;
  border-top: 3px solid #607d8b;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 12px;
}

.answers-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.answer-item {
  position: relative;
  padding: 18px 24px;
  background-color: #ffffff;
  border-radius: 8px;
  transition: background-color 0.25s ease, border-color 0.25s ease;
  border: 1px solid transparent;
}

.answer-item:hover {
  background-color: #f1f3f4;
  border-color: #dfe3e8;
}

.answer-main {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.answer-content-time-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.answer-content {
  color: #212121;
  font-size: 16px;
  font-weight: 500;
  line-height: 1.7;
  flex: 1;
  word-break: break-word;
  white-space: pre-line;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 80%;
}

.meta-time {
  color: #757575;
  font-size: 13px;
  white-space: nowrap;
  flex-shrink: 0;
}

.meta-row-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.answer-like {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #ff6f00;
  font-size: 13px;
}

.source-title {
  color: #2962ff;
  cursor: pointer;
  font-weight: 500;
  margin-left: 2px;
  transition: color 0.2s;
}

.source-title:hover {
  color: #0039cb;
}

.meta-like {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #ff6f00;
  font-size: 14px;
}

.like-num {
  font-weight: 500;
}

.answer-item.skeleton {
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

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

.answers-pagination {
  margin: 32px 0;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .answer-item {
    padding: 16px;
  }

  .answer-content {
    font-size: 15px;
  }
}
</style>
