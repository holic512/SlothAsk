<template>
  <div class="question-container">
    <!-- 引入筛选组件 -->
    <FilterSection
        v-model:searchText="searchText"
        v-model:filterCategory="filterCategory"
        v-model:filterTag="filterTag"
        v-model:filterType="filterType"
        v-model:filterDifficulty="filterDifficulty"
    />

    <!-- 题目列表 -->
    <div class="question-list">
      <table>
        <thead>
        <tr>
          <th class="title-col sortable" @click="handleSort('id')" :data-active="sortField === 'id'">
            题目
            <component :is="getSortIcon('id')" class="sort-icon" />
          </th>
          <th class="type-col">
            类型
          </th>
          <th class="number-col sortable" @click="handleSort('viewCount')" :data-active="sortField === 'viewCount'">
            浏览量
            <component :is="getSortIcon('viewCount')" class="sort-icon" />
          </th>
          <th class="number-col sortable" @click="handleSort('passRate')" :data-active="sortField === 'passRate'">
            通过率
            <component :is="getSortIcon('passRate')" class="sort-icon" />
          </th>
          <th class="difficulty-col sortable" @click="handleSort('difficulty')"
              :data-active="sortField === 'difficulty'">
            难度
            <component :is="getSortIcon('difficulty')" class="sort-icon" />
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="question in paginatedQuestions" :key="question.id" @click="handleQuestionClick(question.id)"
            class="question-row">
          <td class="title-col">
            <span class="question-id">{{ question.id }}.</span>
            {{ question.title }}
            <div class="tags">
                <span v-for="(tag, index) in question?.tags.split(',').map(tag => tag.trim()) " :key="index"
                      class="tag">
                  {{ tag }}
                </span>
            </div>
          </td>
          <td class="type-col">
              <span
                  :class="['type-tag', question.type === 1 ? '单选' : question.type === 2 ? '多选' : question.type === 3 ? '判断' : '简答']">
                {{ question.type === 1 ? '单选' : question.type === 2 ? '多选' : question.type === 3 ? '判断' : '简答' }}
              </span>
          </td>
          <td class="number-col">{{ question.view_count }}</td>
          <td class="number-col">{{ }}%</td>
          <td class="difficulty-col">
              <span :class="['difficulty', question.difficulty === 1 ? '简单' : question.difficulty === 2 ? '中等' : '困难']">
                {{ question.difficulty === 1 ? '简单' : question.difficulty === 2 ? '中等' : '困难' }}
              </span>
          </td>
        </tr>
        </tbody>
      </table>

      <div class="pagination-container">
        <el-pagination v-model:current-page="currentPage" :page-size="20" :total="totalQuestions"
                       @current-change="handlePageChange" layout="prev, pager, next" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import FilterSection from "@/view/HomePage/view/StudyPage/components/FilterSection.vue";
import { ref, computed, watch } from 'vue';
import { useQuestionBankStore } from '@/view/HomePage/view/store/QuestionBank';
import { useRouter } from 'vue-router';
import {
  ArrowUp,
  ArrowDown,
} from '@element-plus/icons-vue';

interface SortableField {
  id: number;
  viewCount: number;
  passRate: number;
  difficulty: string;
  view_count: number;
}

const questionBank = useQuestionBankStore();// 获取store
const selectedCategory = ref('all');// 初始化
const searchText = ref('');// 搜索框
const filterDifficulty = ref<number>(0);// 难度
const filterCategory = ref<number>(0);// 分类
const filterTag = ref('');// 标签
const filterType = ref<number>(0);// 类型
const sortField = ref<keyof SortableField | ''>('');// 排序字段
const sortOrder = ref<'asc' | 'desc'>('asc');// 排序顺序

// 筛选逻辑
const filteredQuestions = computed(() => {
  let questions = selectedCategory.value === 'all'
      ? questionBank.questions
      : questionBank.questions.filter(q => q.category_id === Number(selectedCategory.value));

  if (filterCategory.value !== 0) {
    questions = questions.filter(q => q.category_id === Number(filterCategory.value));
  }

  if (filterDifficulty.value !== 0) {
    questions = questions.filter(q => q.difficulty === filterDifficulty.value);
  }

  if (searchText.value) {
    const searchLower = searchText.value.toLowerCase();
    questions = questions.filter(q =>
        q.id.toString().includes(searchText.value) ||
        q.title.toLowerCase().includes(searchLower)
    );
  }

  if (filterTag.value) {
    questions = questions.filter(q => q.tags?.includes(filterTag.value));
  }

  if (filterType.value !== 0) {
    questions = questions.filter(q => q.type === filterType.value);
  }

  if (sortField.value) {
    questions = [...questions].sort((a, b) => {
      let aValue, bValue;

      switch (sortField.value) {
        case 'difficulty':
          aValue = a.difficulty;
          bValue = b.difficulty;
          break;
        case 'viewCount':
          aValue = a.view_count || 0;
          bValue = b.view_count || 0;
          break;
        default:
          aValue = a[sortField.value];
          bValue = b[sortField.value];
      }

      if (sortOrder.value === 'asc') {
        return aValue > bValue ? 1 : -1;
      } else {
        return aValue < bValue ? 1 : -1;
      }
    });
  }

  return questions;
});

const currentPage = ref(1);// 当前页码

const totalQuestions = computed(() => filteredQuestions.value.length);// 总题目数

// 分页逻辑
const paginatedQuestions = computed(() => {
  const questions = filteredQuestions.value;
  const start = (currentPage.value - 1) * 20;
  const end = start + 20;
  return questions.slice(start, end);
});

// 处理页码变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

// 监听筛选条件变化
watch([filterCategory, filterDifficulty, filterTag, filterType, searchText], () => {
  currentPage.value = 1;
});

// 处理排序
const handleSort = (field: keyof SortableField | '') => {
  if (sortField.value === field) {
    // 如果点击的是当前排序字段，则切换排序顺序
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    // 如果点击的是新字段，则设置为升序
    sortField.value = field;
    sortOrder.value = 'asc';
  }
};

// 获取排序图标
const getSortIcon = (field: keyof SortableField | '') => {
  if (sortField.value !== field) {
    return null;  // 返回 null 而不是空白字符
  }
  return sortOrder.value === 'asc' ? ArrowUp : ArrowDown;
};

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
</script>

<style scoped>
.question-container {
  padding-top: 12px;
}

.question-list {
  width: 100%;
  overflow-x: auto;
  max-width: 100%;
  margin-top: 12px; /* 添加表格上方的间距 */
}
.question-list tbody tr:nth-child(odd) {
  background-color: #ffffff;
}

.question-list tbody tr:nth-child(even) {
  background-color: #f5f5f5;
}



table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  table-layout: fixed; /* 固定表格布局 */
}

th,
td {
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
  word-wrap: break-word; /* 防止单词溢出 */
  overflow: hidden; /* 避免内容溢出 */
}

th {
  font-weight: 500;
  color: #666;
  background: #fafafa;
}

.title-col {
  min-width: 250px; /* 最小宽度，保证题目列有足够空间 */
  width: 35%; /* 题目列占比 */
}

.type-col {
  width: 15%; /* 类型列占比 */
}

.number-col {
  width: 15%; /* 浏览量、通过率列占比 */
}

.difficulty-col {
  width: 15%; /* 难度列占比 */
  text-align: center;
}

.difficulty {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  display: inline-block;
  min-width: 36px;
  text-align: center;
}

.difficulty.简单 {
  color: #52c41a;
  background: #f6ffed;
}

.difficulty.中等 {
  color: #faad14;
  background: #fff7e6;
}

.difficulty.困难 {
  color: #ff4d4f;
  background: #fff1f0;
}

.tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 12px;
  background: #f0f0f0;
  color: #666;
}

.type-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.type-tag.单选 {
  color: #1890ff;
  background: #e6f7ff;
}

.type-tag.多选 {
  color: #722ed1;
  background: #f9f0ff;
}

.type-tag.判断 {
  color: #52c41a;
  background: #f6ffed;
}

.type-tag.简答 {
  color: #fa8c16;
  background: #fff7e6;
}

tr:hover {
  background: #fafafa;
}

td {
  font-size: 14px;
  color: #333;
}

.question-id {
  color: #999;
  margin-right: 8px;
  font-family: Consolas, Monaco, monospace;
}

.sortable {
  cursor: pointer;
  user-select: none;
  position: relative;
  padding-right: 24px;
}

.sortable:hover {
  background: #f5f5f5;
}

.sort-icon {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  opacity: 0.5;
  transition: opacity 0.2s;
  width: 14px;
  height: 14px;
}

</style>
