<template>
  <div class="question-container">
    <!-- 引入筛选组件 -->
    <FilterSection/>

    <!-- 题目列表 -->
    <div class="question-list">
      <el-table :data="loading ? skeletonData : questionBankStore.questions"
                style="width: 100%"
                stripe
                :row-class-name="tableRowClassName"
      >
        <el-table-column :show-overflow-tooltip="true" label="标题" min-width="350" prop="title">
          <template #default="scope">
            <!-- 骨架屏状态 -->
            <div v-if="loading" class="skeleton-item">
              <el-skeleton animated>
                <template #template>
                  <div class="skeleton-title">
                    <el-skeleton-item style="width: 30px; margin-right: 8px;" variant="text" />
                    <el-skeleton-item style="width: 60%;" variant="text" />
                  </div>
                </template>
              </el-skeleton>
            </div>
            <!-- 正常数据状态 -->
            <el-popover
                v-else
                :show-after="1500"
                :width="200"
                trigger="hover"
            >
              <template #default>
                <div class="question-detail">
                  <div class="detail-item">
                    <span class="detail-label">分类:</span>
                    <el-tag effect="plain" size="small" type="info">
                      {{ getCategoryById(scope.row.categoryId) }}
                    </el-tag>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">标签:</span>
                    <div class="tags-container">
                      <el-tag 
                        v-for="tagId in scope.row.tagCategoryIds" 
                        :key="tagId" 
                        class="tag-item"
                        effect="plain" 
                        size="small"
                        type="success"
                      >
                        {{ getTagName(tagId) }}
                      </el-tag>
                      <span v-if="!scope.row.tagCategoryIds || scope.row.tagCategoryIds.length === 0" class="no-tags">
                        无标签
                      </span>
                    </div>
                  </div>
                </div>
              </template>
              <template #reference>
                <el-text class="question-link" @click="handleQuestionClick(scope.row.virtualId)">
                  <span class="question-index">{{ scope.row.id }}. </span>
                  {{ scope.row.title }}
                </el-text>
              </template>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column label="难度" prop="difficulty" width="80">
          <template #default="scope">
            <!-- 骨架屏状态 -->
            <el-skeleton v-if="loading" animated>
              <template #template>
                <el-skeleton-item style="width: 40px;" variant="text" />
              </template>
            </el-skeleton>
            <!-- 正常数据状态 -->
            <span v-else :class="getDifficultyClass(scope.row.difficulty)">
              {{ getDifficultyLabel(scope.row.difficulty) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="类型" prop="type" width="80">
          <template #default="scope">
            <!-- 骨架屏状态 -->
            <el-skeleton v-if="loading" animated>
              <template #template>
                <el-skeleton-item style="width: 40px;" variant="text" />
              </template>
            </el-skeleton>
            <!-- 正常数据状态 -->
            <span v-else :class="getQuestionTypeClass(scope.row.type)">
              {{ getQuestionTypeLabel(scope.row.type) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="热度" prop="viewCount" width="100">
          <template #default="scope">
            <!-- 骨架屏状态 -->
            <div v-if="loading" class="heat-bar-container">
              <el-skeleton animated>
                <template #template>
                  <div class="skeleton-heat-bar">
                    <el-skeleton-item style="width: 8px; height: 12px; margin-right: 1px;" variant="text" />
                    <el-skeleton-item style="width: 8px; height: 12px; margin-right: 1px;" variant="text" />
                    <el-skeleton-item style="width: 8px; height: 12px; margin-right: 1px;" variant="text" />
                    <el-skeleton-item style="width: 8px; height: 12px; margin-right: 1px;" variant="text" />
                    <el-skeleton-item style="width: 8px; height: 12px;" variant="text" />
                  </div>
                </template>
              </el-skeleton>
            </div>
            <!-- 正常数据状态 -->
            <el-tooltip v-else :content="`浏览量: ${scope.row.viewCount}`" placement="top">
              <div class="heat-bar-container">
                <div class="heat-bar">
                  <span
                      v-for="(bar, index) in getHeatBars(scope.row.viewCount)"
                      :key="index"
                      :class="['heat-bar-item', bar.class]"
                  >
                    ||
                  </span>
                </div>
              </div>
            </el-tooltip>
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
import FilterSection from "@/view/HomePage/view/StudyPage/components/LeftBox/Filter/FilterSection.vue";
import {onMounted, reactive, ref, watch} from 'vue';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import {useRouter} from 'vue-router';
// 移除不再使用的图标导入
import {apiGetQuestionList} from '../../service/ApiGetQuestionList'
import {debounce} from 'lodash-es';
import {useScrollbarStore} from '@/pinia/ScrollbarStore';

const questionBankStore = useQuestionBankStore()
const loading = ref(false); // 添加 loading 状态

// 骨架屏数据 - 固定20行
const skeletonData = ref(Array.from({ length: 20 }, (_, index) => ({
  id: index + 1,
  title: '',
  difficulty: 1,
  type: 1,
  viewCount: 0,
  categoryId: 0,
  tagCategoryIds: [],
  virtualId: ''
})))
// 用于存储上一次的请求参数，避免重复请求
const prevParams = reactive({
  searchText: '',
  filterCategory: 0,
  filterType: 0,
  filterDifficulty: 0,
  filterTags: [] as number[],
  pageNum: 1,
  // 新增的高级筛选字段
  matchAllConditions: true,
  filterCategoryEquals: null as number | null,
  filterCategoryNotEquals: null as number | null,
  filterTypeEquals: null as number | null,
  filterTypeNotEquals: null as number | null,
  filterDifficultyEquals: null as number | null,
  filterDifficultyNotEquals: null as number | null
});

// 获取分类 的名字
const getCategoryById = (id: number) => {
  return questionBankStore.FilterCategoryList.find((Category) => Category.id === id)?.name;
}

// 获取难度标签文字
const getDifficultyLabel = (id: number) => {
  const difficultyMap = {
    1: "简单",
    2: "中等",
    3: "困难"
  };
  return difficultyMap[id] || "未知";
};

// 获取难度样式类名
const getDifficultyClass = (id: number) => {
  const classMap = {
    1: "difficulty-easy",
    2: "difficulty-medium",
    3: "difficulty-hard"
  };
  return classMap[id] || "difficulty-unknown";
};

// 获取问题类型标签文字
const getQuestionTypeLabel = (id: number) => {
  const typeMap = {
    1: "单选",
    2: "多选",
    3: "判断",
    4: "简答"
  };
  return typeMap[id] || "未知";
};

// 获取问题类型样式类名
const getQuestionTypeClass = (id: number) => {
  const classMap = {
    1: "type-single",
    2: "type-multiple",
    3: "type-judge",
    4: "type-essay"
  };
  return classMap[id] || "type-unknown";
};

// 检查参数是否有变化
const hasParamsChanged = () => {
  const currentParams = questionBankStore.pagination;

  return (
      prevParams.searchText !== currentParams.searchText ||
      prevParams.filterCategory !== currentParams.filterCategory ||
      prevParams.filterType !== currentParams.filterType ||
      prevParams.filterDifficulty !== currentParams.filterDifficulty ||
      prevParams.pageNum !== currentParams.pageNum ||
      JSON.stringify(prevParams.filterTags) !== JSON.stringify(currentParams.filterTags) ||
      // 新增的高级筛选字段比较
      prevParams.matchAllConditions !== currentParams.matchAllConditions ||
      prevParams.filterCategoryEquals !== currentParams.filterCategoryEquals ||
      prevParams.filterCategoryNotEquals !== currentParams.filterCategoryNotEquals ||
      prevParams.filterTypeEquals !== currentParams.filterTypeEquals ||
      prevParams.filterTypeNotEquals !== currentParams.filterTypeNotEquals ||
      prevParams.filterDifficultyEquals !== currentParams.filterDifficultyEquals ||
      prevParams.filterDifficultyNotEquals !== currentParams.filterDifficultyNotEquals
  );
};

// 更新上一次的参数
const updatePrevParams = () => {
  const current = questionBankStore.pagination;
  prevParams.searchText = current.searchText;
  prevParams.filterCategory = current.filterCategory;
  prevParams.filterType = current.filterType;
  prevParams.filterDifficulty = current.filterDifficulty;
  prevParams.filterTags = [...current.filterTags];
  prevParams.pageNum = current.pageNum;
  // 新增的高级筛选字段
  prevParams.matchAllConditions = current.matchAllConditions;
  prevParams.filterCategoryEquals = current.filterCategoryEquals;
  prevParams.filterCategoryNotEquals = current.filterCategoryNotEquals;
  prevParams.filterTypeEquals = current.filterTypeEquals;
  prevParams.filterTypeNotEquals = current.filterTypeNotEquals;
  prevParams.filterDifficultyEquals = current.filterDifficultyEquals;
  prevParams.filterDifficultyNotEquals = current.filterDifficultyNotEquals;
};

// 加载题目数据
const loadQuestions = async () => {
  // 如果参数没有变化，则不重新请求
  if (!hasParamsChanged()) {
    return;
  }

  loading.value = true; // 开始加载
  try {
    const result = await apiGetQuestionList(questionBankStore.pagination)
    if (result.status === 200) {
      questionBankStore.questions = result.data.records
      questionBankStore.pagination.total = result.data.total
      // 更新上一次请求的参数
      updatePrevParams();
    }
  } catch (error) {
    console.error('加载题目失败:', error)
  } finally {
    loading.value = false; // 结束加载
  }
}

// 创建防抖版本的加载函数
const debouncedLoadQuestions = debounce(loadQuestions, 300);

// 页码变化处理
const handlePageChange = (page: number) => {
  questionBankStore.pagination.pageNum = page
  loadQuestions() // 这里可以不使用防抖，因为是用户主动点击触发
}

// 监听过滤条件变化
watch(() => [
  questionBankStore.pagination.searchText,
  questionBankStore.pagination.filterCategory,
  questionBankStore.pagination.filterType,
  questionBankStore.pagination.filterDifficulty,
  questionBankStore.pagination.filterTags,
  // 新增的高级筛选字段
  questionBankStore.pagination.matchAllConditions,
  questionBankStore.pagination.filterCategoryEquals,
  questionBankStore.pagination.filterCategoryNotEquals,
  questionBankStore.pagination.filterTypeEquals,
  questionBankStore.pagination.filterTypeNotEquals,
  questionBankStore.pagination.filterDifficultyEquals,
  questionBankStore.pagination.filterDifficultyNotEquals
], () => {
  // 重置页码并重新加载
  questionBankStore.pagination.pageNum = 1
  debouncedLoadQuestions() // 使用防抖版本避免频繁请求
})

// 初始加载
onMounted(() => {
  loadQuestions()
})

// 处理点击题目
const router = useRouter();
const handleQuestionClick = (questionId: string) => {
  router.push({
    name: 'QuestionPage',
    params: {
      questionId: questionId
    }
  }).then(() => {
    // 导航成功后等待DOM更新完成再滚动
    setTimeout(() => {
      // 先获取scrollbarStore
      const scrollbarStore = useScrollbarStore();
      scrollbarStore.scrollToTop();

      // 备用方案：如果store中的scrollbar引用失效，尝试直接使用DOM API
      if (!scrollbarStore.scrollbarRef) {
        const scrollWrap = document.querySelector('.el-scrollbar__wrap');
        if (scrollWrap) {
          scrollWrap.scrollTop = 0;
        }
      }
    }, 100);
  });
};

const getTagName = (tagId: number): string => {
  const tag = questionBankStore.FilterTagList.find(tag => tag.id === tagId)
  return tag ? tag.name : '未知标签'
}

// 获取热度条显示
const getHeatBars = (viewCount: number) => {
  const bars = []
  const levels = [
    {threshold: 10, class: 'heat-level-1'},
    {threshold: 100, class: 'heat-level-2'},
    {threshold: 1000, class: 'heat-level-3'},
    {threshold: 10000, class: 'heat-level-4'},
    {threshold: 100000, class: 'heat-level-5'}
  ]

  let currentLevel = 0
  for (let i = 0; i < levels.length; i++) {
    if (viewCount >= levels[i].threshold) {
      currentLevel = i + 1
    } else {
      break
    }
  }

  // 生成5个热度条，根据当前等级显示不同颜色
  for (let i = 0; i < 5; i++) {
    if (i < currentLevel) {
      bars.push({class: levels[i].class})
    } else {
      bars.push({class: 'heat-level-inactive'})
    }
  }

  return bars
}

// 获取标签文本
const getTagsText = (tagIds: number[]) => {
  if (!tagIds || tagIds.length === 0) {
    return '无标签'
  }
  return tagIds.map((tagId: number) => getTagName(tagId)).join('、')
}

// 表格行样式类名
const tableRowClassName = ({rowIndex}: { rowIndex: number }) => {
  return 'table-row-hover'
}
</script>

<style scoped>
.question-container {
  min-height: 1000px;
}

.question-list {
  width: 100%;
  overflow-x: auto;
  max-width: 100%;
  margin-top: 12px; /* 添加表格上方的间距 */
}

/* 热度条样式 */
.heat-bar-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  padding-left: 2px;
}

.heat-bar {
  display: flex;
  gap: 1px;
  align-items: center;
}

.heat-bar-item {
  font-size: 12px;
  font-weight: bold;
  line-height: 1;
  transition: color 0.3s ease;
}

/* 热度等级颜色 */
.heat-level-1 {
  color: #52c41a; /* 绿色 - 10+ */
}

.heat-level-2 {
  color: #1890ff; /* 蓝色 - 100+ */
}

.heat-level-3 {
  color: #faad14; /* 橙色 - 1000+ */
}

.heat-level-4 {
  color: #ff4d4f; /* 红色 - 10000+ */
}

.heat-level-5 {
  color: #722ed1; /* 紫色 - 100000+ */
}

.heat-level-inactive {
  color: #d9d9d9; /* 灰色 - 未激活 */
}

/* 表格行悬停样式 */
.table-row-hover {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.table-row-hover:hover {
  background-color: #f5f7fa !important;
}

/* 问题链接样式 */
.question-link {
  display: block;
  width: 100%;
  padding: 4px 0;
}

/* 详情弹窗样式 */
.question-detail {
  padding: 6px 0;
}

.detail-item {
  margin-bottom: 4px;
  display: flex;
  align-items: flex-start;
  gap: 6px;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-label {
  font-weight: bold;
  color: #606266;
  min-width: 60px;
  margin-right: 6px;
  flex-shrink: 0;
}

.detail-value {
  color: #303133;
  flex: 1;
  word-break: break-all;
}

/* 标签容器样式 */
.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
}

.tag-item {
  margin: 0;
}

.no-tags {
  color: #909399;
  font-size: 12px;
  font-style: italic;
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

/* 难度标签样式 */
.difficulty-easy {
  color: #52c41a;
  font-weight: 500;
}

.difficulty-medium {
  color: #faad14;
  font-weight: 500;
}

.difficulty-hard {
  color: #ff4d4f;
  font-weight: 500;
}

.difficulty-unknown {
  color: #8c8c8c;
  font-weight: 500;
}

/* 问题类型标签样式 */
.type-single {
  color: #52c41a;
  font-weight: 500;
}

.type-multiple {
  color: #faad14;
  font-weight: 500;
}

.type-judge {
  color: #1890ff;
  font-weight: 500;
}

.type-essay {
  color: #ff4d4f;
  font-weight: 500;
}

.type-unknown {
  color: #8c8c8c;
  font-weight: 500;
}

/* 骨架屏样式 */
.skeleton-item {
  padding: 4px 0;
}

.skeleton-title {
  display: flex;
  align-items: center;
}

.skeleton-heat-bar {
  display: flex;
  align-items: center;
  gap: 1px;
}

/* 骨架屏动画优化 */
.el-skeleton.is-animated .el-skeleton__item::after {
  animation-duration: 1.5s;
}
</style>
