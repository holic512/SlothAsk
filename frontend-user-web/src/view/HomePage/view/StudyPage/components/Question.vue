<template>
  <div class="question-container">
    <div v-if="showCategorySelect" class="select-list-container">
      <div class="select-list" ref="selectList">
        <div class="select-item" 
          :class="{ active: selectedCategory === 'all' }"
          @click="handleSelect('all')"
        >
          <span class="icon">📚</span>
          <span class="name">全部题目</span>
        </div>

        <div class="select-item" 
          v-for="item in displayedCategories" 
          :key="item.id"
          :class="{ active: selectedCategory === item.id }"
          @click="handleSelect(item.id)"
        >
          <span class="icon">{{ item.icon }}</span>
          <span class="name">{{ item.name }}</span>
        </div>
      </div>
      
      <div v-show="showScrollButton" class="scroll-button-container">
        <button 
          class="scroll-button"
          @click="scrollRight"
          ref="scrollButton"
        >
          <el-icon><ArrowRight /></el-icon>
        </button>
      </div>
    </div>

    <div class="filter-row">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchText"
          placeholder="搜索题目ID或标题..."
          class="search-input"
        >
      </div>
      
      <div class="filter-group">
        <select v-model="filterCategory" class="filter-select">
          <option value="">全部分类</option>
          <option v-for="category in allCategories" 
            :key="category.id" 
            :value="category.id"
          >
            {{ category.name }}
          </option>
        </select>

        <select v-model="filterType" class="filter-select">
          <option value="">全部类型</option>
          <option v-for="type in allTypes" 
            :key="type" 
            :value="type"
          >
            {{ type }}
          </option>
        </select>

        <select v-model="filterDifficulty" class="filter-select">
          <option value="">全部难度</option>
          <option value="简单">简单</option>
          <option value="中等">中等</option>
          <option value="困难">困难</option>
        </select>

        <button 
          class="tag-button"
          :class="{ active: showTagFilter }"
          @click="toggleTagFilter"
        >
          标签
          <span v-if="filterTag" class="selected-tag">{{ filterTag }}</span>
        </button>
      </div>
    </div>

    <div v-if="showTagFilter" class="tag-filter-container">
      <div class="tag-search">
        <div class="search-input-wrapper">
          <el-icon class="search-icon"><Search /></el-icon>
          <input 
            type="text" 
            v-model="tagSearchText"
            placeholder="搜索标签..."
            class="tag-search-input"
          >
          <el-icon 
            v-if="tagSearchText" 
            class="clear-icon"
            @click="tagSearchText = ''"
          >
            <Close />
          </el-icon>
        </div>
      </div>
      <div class="tag-filter">
        <span 
          v-for="tag in filteredTags" 
          :key="tag"
          :class="['filter-tag', { active: filterTag === tag }]"
          @click="handleTagSelect(tag)"
        >
          {{ tag }}
        </span>
      </div>
    </div>

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
            <th class="number-col sortable" @click="handleSort('comments')" :data-active="sortField === 'comments'">
              评论
              <component :is="getSortIcon('comments')" class="sort-icon" />
            </th>
            <th class="number-col sortable" @click="handleSort('passRate')" :data-active="sortField === 'passRate'">
              通过率
              <component :is="getSortIcon('passRate')" class="sort-icon" />
            </th>
            <th class="difficulty-col sortable" @click="handleSort('difficulty')" :data-active="sortField === 'difficulty'">
              难度
              <component :is="getSortIcon('difficulty')" class="sort-icon" />
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="question in paginatedQuestions" 
            :key="question.id"
            @click="handleQuestionClick(question.id)"
            class="question-row"
          >
            <td class="title-col">
              <span class="question-id">{{ question.id }}.</span>
              {{ question.title }}
              <div class="tags">
                <span v-for="tag in question.tags" 
                  :key="tag" 
                  class="tag">
                  {{ tag }}
                </span>
              </div>
            </td>
            <td class="type-col">
              <span :class="['type-tag', question.type]">
                {{ question.type }}
              </span>
            </td>
            <td class="number-col">{{ question.comments }}</td>
            <td class="number-col">{{ question.passRate }}%</td>
            <td class="difficulty-col">
              <span :class="['difficulty', question.difficulty]">
                {{ question.difficulty }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="20"
          :total="totalQuestions"
          @current-change="handlePageChange"
          layout="prev, pager, next"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, h, onMounted, onUnmounted, watch } from 'vue';
import { useQuestionBankStore } from '../../pinia/QuestionBank';
import { useRouter } from 'vue-router';
import { 
  ArrowUp, 
  ArrowDown, 
  Search, 
  Close, 
  ArrowRight,
  ChatLineRound 
} from '@element-plus/icons-vue';
import { ElPagination, ElIcon } from 'element-plus';

interface SortableField {
  id: number;
  comments: number;
  passRate: number;
  difficulty: string;
}

interface Props {
  selectedCategory?: string | number;
  showCategorySelect?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  selectedCategory: 'all',
  showCategorySelect: true
});

const questionBank = useQuestionBankStore();
const displayedCategories = computed(() => questionBank.getDisplayCategories);
const allCategories = computed(() => questionBank.getAllCategories);
const allTypes = computed(() => questionBank.getAllTypes);
const selectedCategory = ref(props.selectedCategory);
const searchText = ref('');
const filterDifficulty = ref('');
const filterCategory = ref('');
const filterTag = ref('');
const filterType = ref('');
const sortField = ref<keyof SortableField | ''>('');
const sortOrder = ref<'asc' | 'desc'>('asc');

// 修改获取标签的计算属性
const availableTags = computed(() => {
  // 如果是在分类详情页面（非全部分类）
  if (selectedCategory.value !== 'all') {
    const currentCategory = questionBank.categories.find(
      c => c.id === Number(selectedCategory.value)
    );
    if (currentCategory) {
      // 获取当前分类下所有题目的标签
      const tagSet = new Set<string>();
      currentCategory.questions.forEach(question => {
        question.tags?.forEach(tag => tagSet.add(tag));
      });
      return Array.from(tagSet).sort();
    }
  }
  // 如果是全部分类，则返回所有标签
  return questionBank.getAllTags;
});

const selectList = ref<HTMLElement | null>(null);
const showScrollButton = ref(false);
const scrollButton = ref<HTMLElement | null>(null);

// 检查滚动条
const checkScroll = (): void => {
  if (selectList.value) {
    const { scrollWidth, clientWidth, scrollLeft } = selectList.value;
    const isAtEnd = Math.ceil(scrollLeft + clientWidth) >= scrollWidth;
    showScrollButton.value = scrollWidth > clientWidth;
    if (isAtEnd && scrollButton.value) {
      scrollButton.value.classList.add('at-end');
    } else if (scrollButton.value) {
      scrollButton.value.classList.remove('at-end');
    }
  }
};

// 滚动右侧按钮
const scrollRight = (): void => {
  if (selectList.value) {
    const { scrollWidth, clientWidth, scrollLeft } = selectList.value;
    const isAtEnd = Math.ceil(scrollLeft + clientWidth) >= scrollWidth;

    if (isAtEnd) {
      selectList.value.scrollTo({ left: 0, behavior: 'smooth' });
    } else {
      selectList.value.scrollBy({ left: 200, behavior: 'smooth' });
    }
  }
};

// 监听滚动事件
onMounted(() => {
  checkScroll();
  window.addEventListener('resize', checkScroll);
  if (selectList.value) {
    selectList.value.addEventListener('scroll', checkScroll);
  }
});

onUnmounted(() => {
  window.removeEventListener('resize', checkScroll);
  if (selectList.value) {
    selectList.value.removeEventListener('scroll', checkScroll);
  }
});

// 计算筛选后的题目
const filteredQuestions = computed(() => {
  let questions = selectedCategory.value === 'all' 
    ? questionBank.getAllQuestions 
    : questionBank.getCategoryQuestions(selectedCategory.value);

  // 按分类筛选
  if (filterCategory.value) {
    questions = questionBank.getCategoryQuestions(filterCategory.value);
  }

  // 按难度筛选
  if (filterDifficulty.value) {
    questions = questions.filter(q => q.difficulty === filterDifficulty.value);
  }

  // 按题目名称或ID搜索
  if (searchText.value) {
    const searchLower = searchText.value.toLowerCase();
    questions = questions.filter(q => 
      // 匹配题目ID
      q.id.toString().includes(searchText.value) ||
      // 匹配题目标题
      q.title.toLowerCase().includes(searchLower)
    );
  }

  // 按标签筛选
  if (filterTag.value) {
    questions = questions.filter(q => 
      q.tags?.includes(filterTag.value)
    );
  }

  // 按类型筛选
  if (filterType.value) {
    questions = questions.filter(q => q.type === filterType.value);
  }

  // 排序
  if (sortField.value) {
    questions = [...questions].sort((a, b) => {
      let aValue = sortField.value === 'difficulty' 
        ? getDifficultyValue(a[sortField.value])
        : a[sortField.value];
      let bValue = sortField.value === 'difficulty'
        ? getDifficultyValue(b[sortField.value])
        : b[sortField.value];

      if (sortOrder.value === 'asc') {
        return aValue > bValue ? 1 : -1;
      } else {
        return aValue < bValue ? 1 : -1;
      }
    });
  }

  return questions;
});

// 处理分类选择
const handleSelect = (id: string | number) => {
  selectedCategory.value = id;
};

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

// 获取难度值
const getDifficultyValue = (difficulty: string): number => {
  switch (difficulty) {
    case '简单': return 1;
    case '中等': return 2;
    case '困难': return 3;
    default: return 0;
  }
};

const showTagFilter = ref(false);


const toggleTagFilter = () => {
  showTagFilter.value = !showTagFilter.value;
};

const handleTagSelect = (tag: string) => {
  filterTag.value = filterTag.value === tag ? '' : tag;
};

const tagSearchText = ref('');

// 过滤标签
const filteredTags = computed(() => {
  if (!tagSearchText.value) return availableTags.value;
  
  const searchText = tagSearchText.value.toLowerCase();
  return availableTags.value.filter(tag => 
    tag.toLowerCase().includes(searchText)
  );
});

const currentPage = ref(1);

// 更新总数
const totalQuestions = computed(() => filteredQuestions.value.length);

// 分页后的题目
const paginatedQuestions = computed(() => {
  const questions = filteredQuestions.value;
  const start = (currentPage.value - 1) * 20;
  const end = start + 20;
  return questions.slice(start, end);
});

const handlePageChange = (page: number) => {
  currentPage.value = page;
  // 可选：滚动到页面顶部
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

// 当筛选条件改变时重置分页
watch([filterCategory, filterDifficulty, filterTag, filterType, searchText], () => {
  currentPage.value = 1;
});

const router = useRouter();

const handleQuestionClick = (questionId: number) => {
  router.push({
    name: 'QuestionDetail',
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

.select-list-container {
  position: relative;
  margin-bottom: 24px;
}

.select-list {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding: 4px 0;
  -ms-overflow-style: none;
  scrollbar-width: none;
  scroll-behavior: smooth;
}

.scroll-button-container {
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 80px;
  background: linear-gradient(90deg, transparent, #fff 40%);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 8px;
}

.scroll-button {
  position: relative;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: white;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  z-index: 1;
}

.scroll-button.at-end {
  transform: rotate(180deg);
}

.scroll-button:hover {
  background: #f5f5f5;
  border-color: #d9d9d9;
}

.scroll-button .el-icon {
  font-size: 16px;
  color: #666;
}

/* 给最后一个select-item添加右边距，避免被渐变遮挡 */
.select-item:last-child {
  margin-right: 80px;
}

.select-item {
  display: flex;
  align-items: center;
  padding: 8px 20px;
  border-radius: 50px;
  background: #f5f5f5;
  color: #333;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
  white-space: nowrap;
  flex-shrink: 0;
  height: 36px;
  box-sizing: border-box;
}

.select-item:hover {
  background: #eee;
  transform: translateY(-1px);
}

.select-item.active {
  background: #333;
  color: #fff;
  border-color: #444;
}

.icon {
  font-size: 18px;
  margin-right: 6px;
}

.name {
  font-size: 14px;
  font-weight: 500;
}

.filter-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0;
  gap: 16px;
}

.search-box {
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 8px 16px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #333;
}

.filter-group {
  display: flex;
  gap: 12px;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  font-size: 14px;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-select:focus {
  outline: none;
  border-color: #333;
}

.filter-select:hover {
  border-color: #d9d9d9;
}

.question-list {
  width: 100%;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

th, td {
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
}

th {
  font-weight: 500;
  color: #666;
  background: #fafafa;
}

.title-col {
  min-width: 300px;
}

.difficulty-col {
  width: 100px;
  text-align: center;
  padding-right: 24px; /* 给排序图标留出空间 */
}

.difficulty {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  display: inline-block; /* 确保标签居中 */
  min-width: 36px; /* 设置最小宽度使标签对齐 */
  text-align: center; /* 标签文字居中 */
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

.sortable:hover .sort-icon {
  opacity: 1;
}

/* 当前排序列的样式 */
.sortable[data-active="true"] {
  color: #333;
}

.sortable[data-active="true"] .sort-icon {
  opacity: 1;
  color: #333;
}

.tags {
  margin-top: 4px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 12px;
  background: #f0f0f0;
  color: #666;
}

.tag-button {
  padding: 8px 16px;
  border-radius: 4px;
  border: 1px solid #e8e8e8;
  background: white;
  color: #333;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.tag-button:hover {
  border-color: #d9d9d9;
}

.tag-button.active {
  background: #f5f5f5;
  border-color: #333;
}

.selected-tag {
  padding: 2px 8px;
  background: #f0f0f0;
  border-radius: 12px;
  font-size: 12px;
  color: #666;
}

.tag-filter-container {
  margin-top: 12px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 12px;
  border: 1px solid #eee;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.tag-search {
  margin-bottom: 16px;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 12px;
  color: #999;
  font-size: 16px;
}

.clear-icon {
  position: absolute;
  right: 12px;
  color: #999;
  cursor: pointer;
  font-size: 16px;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.2s;
}

.clear-icon:hover {
  background: #f0f0f0;
  color: #666;
}

.tag-search-input {
  width: 100%;
  padding: 10px 36px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  font-size: 14px;
  background: white;
  transition: all 0.3s;
}

.tag-search-input:focus {
  outline: none;
  border-color: #333;
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.05);
}

.tag-search-input::placeholder {
  color: #999;
}

.tag-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-tag {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 16px;
  background: white;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #e8e8e8;
}

.filter-tag:hover {
  background: #f5f5f5;
  border-color: #d9d9d9;
}

.filter-tag.active {
  background: #333;
  color: #fff;
  border-color: #444;
}

.type-col {
  width: 80px;
  text-align: center;
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

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding: 16px 0;
}

/* 在 CategoryDetail 中使用时的样式调整 */
:deep(.question-container) {
  background: transparent;
  box-shadow: none;
  padding: 0;
}

.question-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.question-row:hover {
  background-color: #f5f7fa;
}

.number-col {
  text-align: right;
  padding-right: 24px; /* 给排序图标留出空间 */
  width: 100px; /* 固定宽度，使列对齐 */
}
</style>