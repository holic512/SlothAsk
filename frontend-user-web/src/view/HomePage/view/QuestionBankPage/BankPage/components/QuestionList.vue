<template>
  <div class="question-container">
    <!-- 题目列表 -->
    <div class="question-list">
      <el-table :data="paginatedQuestions"
                style="width: 100%"
                stripe
                v-loading="loading"
      >
        <el-table-column prop="title" label="标题" min-width="280" :show-overflow-tooltip="true">
          <template #default="scope">
            <el-link @click="handleQuestionClick(scope.row.virtualId)">
              <span class="question-index">{{ scope.row.id }}. </span>
              {{ scope.row.title }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="difficulty" label="难度" width="90">
          <template #default="scope">
            <el-tag v-bind="getTagProps(scope.row.difficulty)" disable-transitions>
              {{ getTagProps(scope.row.difficulty).label }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="type" label="类型" width="90">
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

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="questionStore.questions.length"
            layout="prev, pager, next"
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
  const tagMap = {
    1: { label: "简单", type: "success" },
    2: { label: "中等", type: "warning" },
    3: { label: "困难", type: "danger" },
  };
  return tagMap[id] || { label: "未知", type: "default" };
};

// 获取类型
const getQuestionTypeProps = (id: number) => {
  const typeMap = {
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
  padding-top: 12px;
  min-height: 1000px;
}

.question-list {
  width: 100%;
  overflow-x: auto;
  max-width: 100%;
  margin-top: 12px; /* 添加表格上方的间距 */
}

.heat-indicator {
  display: flex;
  align-items: center;
  gap: 4px;
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
  margin-right: 4px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>