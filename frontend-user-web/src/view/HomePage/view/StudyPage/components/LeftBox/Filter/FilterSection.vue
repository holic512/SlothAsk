<script setup lang="ts">
import {onMounted, onUnmounted, ref} from 'vue';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import {Connection, Filter, PriceTag, Search} from "@element-plus/icons-vue";
import {
  apiGetCategoryIdAndNameByProjectId
} from "@/view/HomePage/view/StudyPage/service/ApiGetCategoryIdAndNameByProjectId";
import {apiGetTagIdAndName} from "@/view/HomePage/view/StudyPage/service/ApiGetTagIdAndName";
import FilterTag from './FilterTag.vue';
import AdvancedFilter from './AdvancedFilter.vue';

// 获取题库实体类
const questionBank = useQuestionBankStore();

// 获取题库筛选列表 - 分类/标签
onMounted(async () => {
  const categoryResult = await apiGetCategoryIdAndNameByProjectId()
  if (categoryResult.status == 200) {
    questionBank.FilterCategoryList = categoryResult.data
  }

  const tagResult = await apiGetTagIdAndName();
  if (tagResult.status == 200) {
    questionBank.FilterTagList = tagResult.data
  }
})
// 恢复筛选状态
onMounted(() => {
  searchText.value = questionBank.pagination.searchText || '';
  // 使用新的字段结构，但保持向后兼容
  filterCategory.value = questionBank.pagination.filterCategoryEquals ?? questionBank.pagination.filterCategory ?? 0;
  filterTag.value = new Set(questionBank.pagination.filterTags || []);
  filterType.value = questionBank.pagination.filterTypeEquals ?? questionBank.pagination.filterType ?? 0;
  filterDifficulty.value = questionBank.pagination.filterDifficultyEquals ?? questionBank.pagination.filterDifficulty ?? 0;
});

// 防抖函数
const debounce = (fn: Function, delay: number = 300) => {
  let timer: number | null = null;
  return (...args: any[]) => {
    if (timer) clearTimeout(timer);
    timer = window.setTimeout(() => {
      fn(...args);
      timer = null;
    }, delay);
  };
};

// 过滤搜索的变量 - 直接从 store 获取
const searchText = ref('');
const filterCategory = ref<number>(0);
const filterTag = ref<Set<number>>(new Set());
const filterType = ref<number>(0);
const filterDifficulty = ref<number>(0);

// 辅助变量
const showTagFilter = ref(false);
const showAdvancedFilter = ref(false);

// 选择标签 和 删除标签
const handleTagSelect = (tagId: number) => {
  if (filterTag.value.has(tagId)) {
    filterTag.value.delete(tagId);
  } else {
    filterTag.value.add(tagId);
  }

  // 直接更新 store - 传递数组而不是字符串
  const tagsArray = Array.from(filterTag.value);
  updateFilterTag(tagsArray);
};

const clearTags = () => {
  filterTag.value.clear();
  updateFilterTag([]);
};

const toggleTagFilter = () => {
  showTagFilter.value = !showTagFilter.value;
};

const toggleAdvancedFilter = (event: Event) => {
  event.stopPropagation();
  showAdvancedFilter.value = !showAdvancedFilter.value;
};

// 点击外部关闭弹出框
const handleClickOutside = (event: Event) => {
  const target = event.target as HTMLElement;
  const filterDropdown = target.closest('.filter-dropdown');
  if (!filterDropdown && showAdvancedFilter.value) {
    showAdvancedFilter.value = false;
  }
};

// 添加和移除事件监听器
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});

// 更新筛选条件到 store
const updateSearchText = (text: string) => {
  searchText.value = text;
  questionBank.pagination.searchText = text || null;
};

// 防抖处理搜索输入
const debouncedUpdateSearchText = debounce(updateSearchText, 500);

const handleSearchInput = (event: Event) => {
  const text = (event.target as HTMLInputElement).value;
  searchText.value = text; // 立即更新UI显示
  debouncedUpdateSearchText(text); // 延迟更新store和搜索
};

const updateFilterCategory = (category: number) => {
  filterCategory.value = category;
  // 更新新字段结构，同时保持向后兼容
  questionBank.pagination.filterCategoryEquals = category === 0 ? null : category;
  questionBank.pagination.filterCategoryNotEquals = null; // 清除不等于条件
  questionBank.pagination.filterCategory = category === 0 ? null : category; // 向后兼容
};

const updateFilterTag = (tags: number[]) => {
  // 直接传递数组给 store
  questionBank.pagination.filterTags = tags;
};

const updateFilterType = (type: number) => {
  filterType.value = type;
  // 更新新字段结构，同时保持向后兼容
  questionBank.pagination.filterTypeEquals = type === 0 ? null : type;
  questionBank.pagination.filterTypeNotEquals = null; // 清除不等于条件
  questionBank.pagination.filterType = type === 0 ? null : type; // 向后兼容
};

const updateFilterDifficulty = (difficulty: number) => {
  filterDifficulty.value = difficulty;
  // 更新新字段结构，同时保持向后兼容
  questionBank.pagination.filterDifficultyEquals = difficulty === 0 ? null : difficulty;
  questionBank.pagination.filterDifficultyNotEquals = null; // 清除不等于条件
  questionBank.pagination.filterDifficulty = difficulty === 0 ? null : difficulty; // 向后兼容
};

// 处理高级过滤器应用
const handleAdvancedFilters = (filters: {
  category: number;
  difficulty: number;
  type: number;
  categoryOperator: string;
  difficultyOperator: string;
  typeOperator: string;
  matchMode: string;
}) => {
  // 应用过滤条件到store
  if (filters.category > 0) {
    updateFilterCategory(filters.category);
  }
  if (filters.difficulty > 0) {
    updateFilterDifficulty(filters.difficulty);
  }
  if (filters.type > 0) {
    updateFilterType(filters.type);
  }

  // 这里可以添加更多的高级过滤逻辑，比如操作符和匹配模式
  console.log('Applied advanced filters:', filters);
};


</script>

<template xmlns="http://www.w3.org/1999/html">
  <div class="filter-row">
    <div style="display: flex;gap: 8px">

      <div class="search-box">
        <el-icon color="#868686">
          <Search/>
        </el-icon>
        <input v-model="searchText" class="search-input"
               placeholder="搜索题目" @input="handleSearchInput"/>
      </div>

      <div class="filter-box filter-dropdown" @click="toggleAdvancedFilter($event)">
        <el-tooltip :show-after="200" content="条件过滤" placement="top">
          <button class="filter-button" style="background-color: #F5F5F5">
            <el-icon color="#737373" size="16">
              <Filter/>
            </el-icon>
          </button>
        </el-tooltip>

        <!-- 高级过滤弹出框 -->
        <transition mode="out-in" name="advanced-filter-fade">
          <AdvancedFilter
              v-if="showAdvancedFilter"
              @close="showAdvancedFilter = false"
              @apply-filters="handleAdvancedFilters"
          />
        </transition>
      </div>

      <div class="filter-box" @click="toggleTagFilter">
        <el-tooltip :show-after="200" content="标签过滤" placement="top">
          <button class="filter-button" style="background-color: #F5F5F5">
            <el-icon color="#737373" size="16">
              <PriceTag/>
            </el-icon>
          </button>
        </el-tooltip>
      </div>
    </div>

    <!--    right   -->
    <div style="display: flex;">
      <div class="filter-box">
        <el-tooltip :show-after="200" content="随机一题" placement="top">
          <button class="filter-button" style="background-color: #F5F5F5">
            <el-icon color="#737373" size="16">
              <Connection/>
            </el-icon>
          </button>
        </el-tooltip>
      </div>

    </div>
  </div>

  <!-- 标签筛选组件 -->
  <FilterTag
      :selected-tags="filterTag"
      :visible="showTagFilter"
      @tag-select="handleTagSelect"
      @clear-tags="clearTags"
  />

</template>


<style scoped>
.filter-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0;
  gap: 16px;
}

.search-box {
  width: 200px;
  text-align: left;
  border-radius: 32px;
  height: 34px;
  background-color: #F5F5F5;
  padding-left: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.search-input {
  font-size: 14px;
  border: 0 !important;
  background-color: #F5F5F5;
  outline: none !important; /* 强制去除轮廓 */
}

.filter-button {
  height: 34px;
  width: 34px;
  border: none;
  border-radius: 50%;
}

.filter-select {
  width: 150px;
  background: #f5f5f5;
  border: none;
}

.tag-button {
  padding: 8px 24px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.1s;
  display: flex;
  justify-content: flex-start;
}

.tag-button.active {
  background: #f5f5f5;
}


::v-deep(.el-select) {
  width: 150px;
}

.filter-dropdown {
  position: relative;
}

/* 高级过滤器渐入渐出动画 */
.advanced-filter-fade-enter-active,
.advanced-filter-fade-leave-active {
  transition: all 0.15s ease;
}

.advanced-filter-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.advanced-filter-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.advanced-filter-fade-enter-to,
.advanced-filter-fade-leave-from {
  opacity: 1;
  transform: translateY(0) scale(1);
}

</style>
