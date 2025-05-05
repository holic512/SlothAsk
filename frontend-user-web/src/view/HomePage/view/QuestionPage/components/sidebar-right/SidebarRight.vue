<template>
  <div class="sidebar">
    <div class="head">
      <h2 class="title">热门题目</h2>
    </div>
    <div v-loading="loading" class="hot-questions-container">
      <ul v-if="hotQuestions.length > 0" class="hot-questions-list">
        <li v-for="question in hotQuestions" :key="question.virtualId" @click="handleQuestionClick(question.virtualId)"
            class="question-item">
          <div class="question-title">{{ question.title }}</div>
          <div :class="getViewCountClass(question.viewCount)" class="view-count">
            <el-icon v-if="isHot(question.viewCount)"><DataLine /></el-icon>
            <el-icon><View /></el-icon>
            {{ formatViewCount(question.viewCount) }}
          </div>
        </li>
      </ul>
      <div v-else class="empty-state">
        <el-empty description="暂无热门题目" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {DataLine, View} from '@element-plus/icons-vue';
import {ApiGetHotQuestions} from '../../service/ApiGetHotQuestions';

// 定义热门题目的接口
interface HotQuestion {
  virtualId: string;
  title: string;
  viewCount: number;
}

const router = useRouter();
const loading = ref(false);
const hotQuestions = ref<HotQuestion[]>([]);

// 获取热门题目列表
const fetchHotQuestions = async () => {
  loading.value = true;
  try {
    const response = await ApiGetHotQuestions();
    if (response.status === 200) {
      hotQuestions.value = response.data;
    } else {
      ElMessage.error(response.message || '获取热门题目失败');
    }
  } catch (error) {
    console.error('获取热门题目失败:', error);
    ElMessage.error('获取热门题目失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 处理点击题目事件，跳转到对应的题目详情页
const handleQuestionClick = (virtualId: string) => {
  router.push({
    name: 'QuestionPage',
    params: {
      questionId: virtualId
    }
  });
};

// 根据浏览量获取对应的样式类
const getViewCountClass = (viewCount: number): string => {
  if (viewCount >= 1000) {
    return 'view-count-hot';
  } else if (viewCount >= 500) {
    return 'view-count-medium';
  } else {
    return 'view-count-normal';
  }
};

// 判断是否为热门内容
const isHot = (viewCount: number): boolean => {
  return viewCount >= 500;
};

// 格式化浏览量数字
const formatViewCount = (viewCount: number): string => {
  if (viewCount >= 10000) {
    return (viewCount / 10000).toFixed(1) + '万';
  } else if (viewCount >= 1000) {
    return (viewCount / 1000).toFixed(1) + 'k';
  }
  return viewCount.toString();
};

// 组件挂载时获取热门题目
onMounted(() => {
  fetchHotQuestions();
});
</script>

<style scoped>
.sidebar {
  width: 280px;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  margin: 0 auto;
}

.head {
  background: linear-gradient(180deg, #eeb587, #fff);
  border-radius: 8px 8px 0 0;
  padding: 0 18px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.hot-questions-container {
  flex: 1;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #e0e0e0 transparent;
  min-height: 300px;
}

.hot-questions-container::-webkit-scrollbar {
  width: 6px;
}

.hot-questions-container::-webkit-scrollbar-track {
  background: transparent;
}

.hot-questions-container::-webkit-scrollbar-thumb {
  background-color: #e0e0e0;
  border-radius: 3px;
}

.hot-questions-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.question-item {
  padding: 12px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s ease;
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.question-item:hover {
  background-color: #f5f7fa;
}

.question-title {
  font-size: 14px;
  color: #333;
  flex: 1;
  margin-right: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.view-count {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.view-count-normal {
  color: #909399;
}

.view-count-medium {
  color: #e6a23c;
  font-weight: 500;
}

.view-count-hot {
  color: #f56c6c;
  font-weight: bold;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  min-height: 200px;
}
</style>