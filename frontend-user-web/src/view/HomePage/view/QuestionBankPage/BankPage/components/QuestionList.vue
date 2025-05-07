<template>
  <div class="question-container">
    <!-- 题目列表 -->
    <div class="question-list">
      <el-table :data="paginatedQuestions"
                style="width: 100%"
                stripe
                v-loading="loading"
                :default-sort="{ prop: 'id', order: 'ascending' }"
                :size="tableSize"
      >
        <el-table-column prop="title" label="标题" min-width="280" :show-overflow-tooltip="true">
          <template #default="scope">
            <el-link @click="handleQuestionClick(scope.row.virtualId)">
              <span class="question-index">{{ scope.row.id }}. </span>
              {{ scope.row.title }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column :visible="screenWidth > 768" label="难度" prop="difficulty" width="90">
          <template #default="scope">
            <el-tag v-bind="getTagProps(scope.row.difficulty)" disable-transitions>
              {{ getTagProps(scope.row.difficulty).label }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column :visible="screenWidth > 576" label="类型" prop="type" width="90">
          <template #default="scope">
            <el-tag v-bind="getQuestionTypeProps(scope.row.type)" disable-transitions>
              {{ getQuestionTypeProps(scope.row.type).label }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="viewCount" label="热度" width="70">
          <template #default="scope">
            <div class="heat-indicator">
              <template v-if="scope.row.viewCount >= 1000">
                <el-icon class="hot-icon">
                  <Histogram/>
                </el-icon>
                <span class="text-red-500">火爆</span>
              </template>
              <template v-else-if="scope.row.viewCount >= 500">
                <el-icon class="warm-icon">
                  <TrendCharts/>
                </el-icon>
                <span class="text-orange-500">热门</span>
              </template>
              <template v-else-if="scope.row.viewCount >= 100">
                <el-icon class="normal-icon">
                  <Star/>
                </el-icon>
                <span class="text-blue-500">常见</span>
              </template>
              <template v-else>
                <el-icon class="cool-icon">
                  <View/>
                </el-icon>
                <span class="text-gray-500">{{ scope.row.viewCount }}</span>
              </template>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 移动设备视图 - 条目列表 -->
      <div v-if="screenWidth <= 576" class="mobile-question-list">
        <div v-for="question in paginatedQuestions" :key="question.id" class="mobile-question-item" @click="handleQuestionClick(question.virtualId)">
          <div class="mobile-question-title">
            <span class="question-index">{{ question.id }}. </span>
            {{ question.title }}
          </div>
          <div class="mobile-question-meta">
            <el-tag class="mobile-tag" size="small" v-bind="getTagProps(question.difficulty)">
              {{ getTagProps(question.difficulty).label }}
            </el-tag>
            <el-tag class="mobile-tag" size="small" v-bind="getQuestionTypeProps(question.type)">
              {{ getQuestionTypeProps(question.type).label }}
            </el-tag>
            <div class="heat-indicator mobile-heat">
              <template v-if="question.viewCount >= 1000">
                <el-icon class="hot-icon"><Histogram/></el-icon>
                <span class="text-red-500">火爆</span>
              </template>
              <template v-else-if="question.viewCount >= 500">
                <el-icon class="warm-icon"><TrendCharts/></el-icon>
                <span class="text-orange-500">热门</span>
              </template>
              <template v-else-if="question.viewCount >= 100">
                <el-icon class="normal-icon"><Star/></el-icon>
                <span class="text-blue-500">常见</span>
              </template>
              <template v-else>
                <el-icon class="cool-icon"><View/></el-icon>
                <span class="text-gray-500">{{ question.viewCount }}</span>
              </template>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="questionStore.questions.length"
            :layout="screenWidth > 576 ? 'prev, pager, next' : 'prev, next'"
            :small="screenWidth <= 768"
            @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {Histogram, Star, TrendCharts, View} from "@element-plus/icons-vue";
import {computed, onMounted, ref, watch} from 'vue';
import {useRouter} from 'vue-router';
import {useQuestionStore} from "@/view/HomePage/view/QuestionBankPage/BankPage/store/QuestionStore";

const loading = ref(false);
const questionStore = useQuestionStore();
const props = defineProps<{ selectedCategory: number }>();

const currentPage = ref(1);
const pageSize = ref(20);
const screenWidth = ref(window.innerWidth);

// 监听窗口大小变化
window.addEventListener('resize', () => {
  screenWidth.value = window.innerWidth;
});

// 根据屏幕宽度计算表格大小
const tableSize = computed(() => {
  if (screenWidth.value <= 768) return 'small';
  return 'default';
});

// 计算当前页的数据
const paginatedQuestions = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return questionStore.questions.slice(start, end);
});

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

// 获取难度
const getTagProps = (id: number) => {
  const tagMap: Record<number, {label: string, type: string}> = {
    1: { label: "简单", type: "success" },
    2: { label: "中等", type: "warning" },
    3: { label: "困难", type: "danger" },
  };
  return tagMap[id] || { label: "未知", type: "default" };
};

// 获取类型
const getQuestionTypeProps = (id: number) => {
  const typeMap: Record<number, {label: string, type: string}> = {
    1: { label: "单选", type: "success" },
    2: { label: "多选", type: "warning" },
    3: { label: "判断", type: "info" },
    4: { label: "简答", type: "danger" },
  };
  return typeMap[id] || { label: "未知", type: "default" };
};

onMounted(async () => {
  loading.value = true;
  await questionStore.fetchQuestionsByCategoryId(props.selectedCategory);
  loading.value = false;
});

// 监听分类变化
watch(() => props.selectedCategory, async () => {
  loading.value = true;
  await questionStore.fetchQuestionsByCategoryId(props.selectedCategory);
  loading.value = false;
});

// 处理点击题目
const router = useRouter();
const handleQuestionClick = (questionId: number) => {
  console.log('questionId:', questionId);
  router.push({
    name: 'QuestionPage',
    params: {
      questionId: questionId.toString()
    }
  });
};
</script>


<style scoped>
.question-container {
  padding-top: 0.75rem;
  min-height: 400px;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
}

.question-list {
  width: 100%;
  max-width: 100%;
  margin-top: 0.75rem;
  box-sizing: border-box;
}

/* 修复Element-UI表格的滚动条样式 */
:deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
}

:deep(.el-table) {
  width: 100% !important;
  box-sizing: border-box;
}

.heat-indicator {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.hot-icon {
  color: #f56c6c;
}

.warm-icon {
  color: #e6a23c;
}

.normal-icon {
  color: #409eff;
}

.cool-icon {
  color: #909399;
}

.question-index {
  color: #909399;
  margin-right: 0.25rem;
}

.pagination-container {
  margin-top: 1.25rem;
  display: flex;
  justify-content: center;
}

/* 移动设备视图样式 */
.mobile-question-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-top: 1rem;
  width: 100%;
  box-sizing: border-box;
}

.mobile-question-item {
  padding: 0.75rem;
  border-radius: 0.5rem;
  background-color: #f9f9f9;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.2s ease;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
}

.mobile-question-item:hover {
  background-color: #f0f0f0;
  transform: translateY(-2px);
}

.mobile-question-title {
  font-size: 0.95rem;
  font-weight: 500;
  margin-bottom: 0.5rem;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mobile-question-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  align-items: center;
}

.mobile-tag {
  font-size: 0.75rem;
}

.mobile-heat {
  margin-left: auto;
  font-size: 0.75rem;
}

@media (max-width: 992px) {
  .question-container {
    padding-top: 0.5rem;
  }
}

@media (max-width: 768px) {
  .pagination-container {
    margin-top: 1rem;
  }
}

@media (max-width: 576px) {
  .question-list > .el-table {
    display: none;
  }
  
  .mobile-question-title {
    font-size: 0.875rem;
  }
}
</style>