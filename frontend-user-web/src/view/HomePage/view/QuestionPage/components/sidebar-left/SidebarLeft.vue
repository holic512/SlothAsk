<template>
  <div class="sidebar">
    <!-- 标题区域 -->
    <div class="sidebar-header">
      <h3 class="category-title">{{ questionList?.categoryName || '题目列表' }}</h3>
      <div class="category-info">
        <el-tag size="small" type="info">
          共 {{ questionList?.totalCount || 0 }} 题
        </el-tag>
        <el-tag size="small" type="success" class="ml-2">
          第 {{ questionList?.currentIndex || 0 }} 题
        </el-tag>
      </div>
    </div>

    <!-- 分页器 -->
    <div class="pagination-container" v-if="questionList?.totalCount > 1">
      <el-pagination
          v-model:current-page="currentPage"
          :page-size="questionList?.pageSize"
          :total="questionList?.totalCount"
          layout="prev, pager, next"
          @current-change="handlePageChange"
      />
    </div>

    <!-- 题目列表 -->
    <div class="question-list-container" v-loading="loading">
      <ul class="question-list" v-if="filteredQuestions.length">
        <li
            v-for="question in filteredQuestions"
            :key="question.virtualId"
            class="question-item"
            :class="{
                'active': question.current,
                'easy': question.difficulty === 1,
                'medium': question.difficulty === 2,
                'hard': question.difficulty === 3
              }"
            @click="handleQuestionClick(question.virtualId)"
        >
          <div class="question-item-content">
            <div class="question-title">{{ question.id }}.{{ question.title }}</div>
            <div class="question-meta">
              <el-tag
                  size="small"
                  :type="getQuestionTypeTag(question.type)"
              >
                {{ getQuestionTypeText(question.type) }}
              </el-tag>
            </div>
          </div>
        </li>
      </ul>
      <el-empty v-else description="暂无题目"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, computed, onMounted, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {QuestionListInterface} from '../../interface/QuestionListInterface';
import {ApiGetCategoryQuestions} from '../../service/ApiGetCategoryQuestions';

// 路由相关
const route = useRoute();
const router = useRouter();

// 状态管理
const loading = ref(false);
const searchQuery = ref('');
const currentPage = ref(1);
const questionList = ref<QuestionListInterface | null>(null);

// 计算属性：过滤后的题目列表
const filteredQuestions = computed(() => {
  if (!questionList.value?.questions) return [];
  if (!searchQuery.value) return questionList.value.questions;

  return questionList.value.questions.filter(question =>
      question.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 获取题目列表数据
const fetchQuestionList = async (virtualId: string) => {
  try {
    loading.value = true;
    const response = await ApiGetCategoryQuestions(virtualId);
    questionList.value = response.data;
    currentPage.value = questionList.value?.currentPage || 1;
  } catch (error) {
    ElMessage.error('获取题目列表失败');
    console.error('获取题目列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 处理题目点击
const handleQuestionClick = (virtualId: string) => {
  router.push({
    name: 'QuestionPage',
    params: {
      questionId: virtualId
    }
  });
};

// 处理搜索清除
const handleSearchClear = () => {
  searchQuery.value = '';
};

// 处理分页变化
const handlePageChange = async (page: number) => {
  currentPage.value = page;
  loading.value = true;
  const response = await ApiGetCategoryQuestions(route.params.questionId as string, page);
  if (response.status === 200) {
    questionList.value = response.data;
  }

  loading.value = false;

};

// 获取题目类型标签
const getQuestionTypeTag = (type: number): string => {
  switch (type) {
    case 1:
      return 'success';
    case 2:
      return 'warning';
    case 3:
      return 'danger';
    case 4:
      return 'primary';
    default:
      return 'info';
  }
};

// 获取题目类型文本
const getQuestionTypeText = (type: number): string => {
  switch (type) {
    case 1:
      return '单选题';
    case 2:
      return '多选题';
    case 3:
      return '判断题';
    case 4:
      return '简答题';
    default:
      return '其他';
  }
};

// 监听路由变化
watch(
    () => route.params.questionId,
    async (newId) => {
      if (newId) {
        loading.value = true;
        try {
          // 先获取数据，不指定页码，使用默认值1或当前页
          const response = await ApiGetCategoryQuestions(newId as string);

          if (response.status === 200) {
            // 检查返回的数据中是否包含当前题目所在页码
            const data = response.data;


            // 如果返回了questionPage且与当前页不同，则加载该页数据
            if (data.questionPage && data.questionPage !== data.currentPage) {
              // 使用题目所在页码重新加载数据
              const pageResponse = await ApiGetCategoryQuestions(newId as string, data.questionPage);
              if (pageResponse.status === 200) {
                questionList.value = pageResponse.data;
                currentPage.value = pageResponse.data.currentPage;
              }
            } else {
              // 正常使用返回的数据
              questionList.value = data;
              currentPage.value = data.currentPage;
            }
          }
        } catch (error) {
          ElMessage.error('获取题目列表失败');
          console.error('获取题目列表失败:', error);
        } finally {
          loading.value = false;
        }
      }
    }
);

// 组件挂载时获取数据
onMounted(async () => {
  if (route.params.questionId) {
    loading.value = true;
    try {
      // 先获取数据，不指定页码，使用默认值1或当前页
      const response = await ApiGetCategoryQuestions(route.params.questionId as string);

      if (response.status === 200) {
        // 检查返回的数据中是否包含当前题目所在页码
        const data = response.data;

        // 如果返回了questionPage且与当前页不同，则加载该页数据
        if (data.questionPage && data.questionPage !== data.currentPage) {
          // 使用题目所在页码重新加载数据
          const pageResponse = await ApiGetCategoryQuestions(route.params.questionId as string, data.questionPage);
          if (pageResponse.status === 200) {
            questionList.value = pageResponse.data;
            currentPage.value = pageResponse.data.currentPage;
          }
        } else {
          // 正常使用返回的数据
          questionList.value = data;
          currentPage.value = data.currentPage;
        }
      }
    } catch (error) {
      ElMessage.error('获取题目列表失败');
      console.error('获取题目列表失败:', error);
    } finally {
      loading.value = false;
    }
  }
});
</script>

<style scoped>
.sidebar {
  width: 300px;
  min-width: 300px;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  overflow: hidden; /* 防止内容溢出 */
}

.sidebar-header {
  padding: 0 8px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.category-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.category-info {
  display: flex;
  gap: 8px;
}

.search-box {
  margin: 16px 0;
  padding: 0 8px;
}

.question-list-container {
  flex: 1;
  overflow: hidden;
  margin: 0 -16px;
  padding: 0 16px;
}

.question-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.question-item {
  padding: 10px 14px;
  margin: 4px 0;
  border-radius: 4px;
  cursor: pointer;
}

.question-item:hover {
  background-color: #f3f4f6;
}

.question-item.active {
  background-color: #e6f7ff;
  border-left: 3px solid #1890ff;
}

.question-item-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.question-title {
  font-size: 14px;
  color: #1f2937;
  line-height: 1.5;
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 难度标识 */
.question-item.easy .question-title {
  border-left: 3px solid #67c23a;
  padding-left: 8px;
}

.question-item.medium .question-title {
  border-left: 3px solid #e6a23c;
  padding-left: 8px;
}

.question-item.hard .question-title {
  border-left: 3px solid #f56c6c;
  padding-left: 8px;
}

.pagination-container {
  padding: 8px;
  display: flex;
  justify-content: center;
}

/* 滚动条样式优化 */
:deep(.el-scrollbar__bar) {
  opacity: 0.3;
}

:deep(.el-scrollbar__bar):hover {
  opacity: 0.8;
}

/* 移动视图下的样式调整 */
@media (max-width: 1350px) {
  .sidebar {
    height: 100vh; /* 在移动视图中占满高度 */
    border-radius: 0; /* 移除圆角 */
  }


  .question-list-container {
    margin: 0;
    padding: 0 8px;
  }
}
</style>