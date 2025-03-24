<template>
  <div class="question-container">
    <!-- 引入筛选组件 -->
    <FilterSection />

    <!-- 题目列表 -->
    <div class="question-list">
      <el-table :data="questionBankStore.questions"
                style="width: 100%"
                stripe
                v-loading="loading"
      >
        <el-table-column prop="title" label="标题" min-width="280" :show-overflow-tooltip="true">
          <template #default="scope">
            <el-link>
              <span class="question-index">{{ scope.$index + 1 }}. </span>
              {{ scope.row.title }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column label="分类" width="140">
          <template #default="scope">
            <el-tag type="info" effect="plain" disable-transitions>
              {{ getCategoryById(scope.row.categoryId) }}
            </el-tag>
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

        <el-table-column prop="tagCategoryIds" label="标签" min-width="160">
          <template #default="scope">
            <el-tag v-for="tagId in scope.row.tagCategoryIds" :key="tagId" type="info" size="small" disable-transitions>
              {{ getTagName(tagId) }}
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

      <div class="pagination-container">
        <el-pagination
            v-model:current-page="questionBankStore.pagination.pageNum"
            :page-size="20"
            :total="questionBankStore.pagination.total"
            @current-change="handlePageChange"
            layout="total, prev, pager, next, jumper"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import FilterSection from "@/view/HomePage/view/StudyPage/components/FilterSection.vue";
import {ref, watch, onMounted} from 'vue';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import {useRouter} from 'vue-router';
import {Histogram, TrendCharts, Star, View} from '@element-plus/icons-vue'
import {apiGetQuestionList} from '../service/ApiGetQuestionList'

const questionBankStore = useQuestionBankStore()
const loading = ref(false); // 添加 loading 状态

// 获取分类 的名字
const getCategoryById = (id: number) => {
  return questionBankStore.FilterCategoryList.find((Category) => Category.id === id)?.name;
}

// 获取难度
const getTagProps = (id: number) => {
  const tagMap = {
    1: {label: "简单", type: "success"},
    2: {label: "中等", type: "warning"},
    3: {label: "困难", type: "danger"},
  };
  return tagMap[id] || {label: "未知", type: "easy"};
};

// 获取类型
const getQuestionTypeProps = (id) => {
  const typeMap = {
    1: {label: "单选", type: "success"}, // 绿色
    2: {label: "多选", type: "warning"}, // 橙色
    3: {label: "判断", type: "info"}, // 蓝色
    4: {label: "简答", type: "danger"}, // 红色
  };
  return typeMap[id] || {label: "未知", type: "default"};
};

// 加载题目数据
const loadQuestions = async () => {
  loading.value = true; // 开始加载
  try {
    const result = await apiGetQuestionList(questionBankStore.pagination)
    if (result.status === 200) {
      questionBankStore.questions = result.data.records
      questionBankStore.pagination.total = result.data.total
    }
  } catch (error) {
    console.error('加载题目失败:', error)
  } finally {
    loading.value = false; // 结束加载
  }
}

// 页码变化处理
const handlePageChange = (page: number) => {
  questionBankStore.pagination.pageNum = page
  loadQuestions()
}

// 监听过滤条件变化
watch(() => [
  questionBankStore.pagination.searchText,
  questionBankStore.pagination.filterCategory,
  questionBankStore.pagination.filterType,
  questionBankStore.pagination.filterDifficulty,
  questionBankStore.pagination.filterTags
], () => {
  // 重置页码并重新加载
  questionBankStore.pagination.pageNum = 1
  loadQuestions()
}, {deep: true})

// 初始加载
onMounted(() => {
  loadQuestions()
})

// 处理点击题目
const router = useRouter();
const handleQuestionClick = (questionId: number) => {
  router.push({
    name: 'QuestionPage',
    params: {
      questionId: questionId.toString()
    }
  });
};

const getTagName = (tagId: number): string => {
  const tag = questionBankStore.FilterTagList.find(tag => tag.id === tagId)
  return tag ? tag.name : '未知标签'
}
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
