<script lang="ts" setup>
import {onMounted, ref, watch} from 'vue';
import {Refresh, Star} from '@element-plus/icons-vue';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';

// 获取题库实体类
const questionBank = useQuestionBankStore();

// 匹配模式
const matchMode = ref(true); // true=全部满足(AND), false=任一满足(OR)

// 过滤条件
const categoryOperator = ref('equals'); // 'equals' 或 'not_equals'
const categoryValue = ref(0);

const difficultyOperator = ref('equals');
const difficultyValue = ref(0);

const typeOperator = ref('equals');
const typeValue = ref(0);

// 初始化时从store恢复状态
onMounted(() => {
  matchMode.value = questionBank.pagination.matchAllConditions;

  // 恢复分类过滤状态
  if (questionBank.pagination.filterCategoryEquals) {
    categoryOperator.value = 'equals';
    categoryValue.value = questionBank.pagination.filterCategoryEquals;
  } else if (questionBank.pagination.filterCategoryNotEquals) {
    categoryOperator.value = 'not_equals';
    categoryValue.value = questionBank.pagination.filterCategoryNotEquals;
  }

  // 恢复难度过滤状态
  if (questionBank.pagination.filterDifficultyEquals) {
    difficultyOperator.value = 'equals';
    difficultyValue.value = questionBank.pagination.filterDifficultyEquals;
  } else if (questionBank.pagination.filterDifficultyNotEquals) {
    difficultyOperator.value = 'not_equals';
    difficultyValue.value = questionBank.pagination.filterDifficultyNotEquals;
  }

  // 恢复类型过滤状态
  if (questionBank.pagination.filterTypeEquals) {
    typeOperator.value = 'equals';
    typeValue.value = questionBank.pagination.filterTypeEquals;
  } else if (questionBank.pagination.filterTypeNotEquals) {
    typeOperator.value = 'not_equals';
    typeValue.value = questionBank.pagination.filterTypeNotEquals;
  }
});

// 重置过滤条件
const resetFilters = () => {
  matchMode.value = true;
  categoryOperator.value = 'equals';
  categoryValue.value = 0;
  difficultyOperator.value = 'equals';
  difficultyValue.value = 0;
  typeOperator.value = 'equals';
  typeValue.value = 0;

  // 同时重置store中的值
  questionBank.pagination.matchAllConditions = true;
  questionBank.pagination.filterCategoryEquals = null;
  questionBank.pagination.filterCategoryNotEquals = null;
  questionBank.pagination.filterDifficultyEquals = null;
  questionBank.pagination.filterDifficultyNotEquals = null;
  questionBank.pagination.filterTypeEquals = null;
  questionBank.pagination.filterTypeNotEquals = null;
};

// 应用过滤条件
const applyFilters = () => {
  // 直接更新store
  questionBank.pagination.matchAllConditions = matchMode.value;

  // 重置所有过滤条件
  questionBank.pagination.filterCategoryEquals = null;
  questionBank.pagination.filterCategoryNotEquals = null;
  questionBank.pagination.filterDifficultyEquals = null;
  questionBank.pagination.filterDifficultyNotEquals = null;
  questionBank.pagination.filterTypeEquals = null;
  questionBank.pagination.filterTypeNotEquals = null;

  // 根据操作符设置对应的值
  if (categoryValue.value > 0) {
    if (categoryOperator.value === 'equals') {
      questionBank.pagination.filterCategoryEquals = categoryValue.value;
    } else {
      questionBank.pagination.filterCategoryNotEquals = categoryValue.value;
    }
  }

  if (difficultyValue.value > 0) {
    if (difficultyOperator.value === 'equals') {
      questionBank.pagination.filterDifficultyEquals = difficultyValue.value;
    } else {
      questionBank.pagination.filterDifficultyNotEquals = difficultyValue.value;
    }
  }

  if (typeValue.value > 0) {
    if (typeOperator.value === 'equals') {
      questionBank.pagination.filterTypeEquals = typeValue.value;
    } else {
      questionBank.pagination.filterTypeNotEquals = typeValue.value;
    }
  }

  // 重置页码
  questionBank.pagination.pageNum = 1;
};

// 监听筛选条件变化，自动应用过滤
watch(
    [matchMode, categoryOperator, categoryValue, difficultyOperator, difficultyValue, typeOperator, typeValue],
    () => {
      applyFilters();
    },
    {deep: true}
);

// 保存为智能题单（暂时禁用）
const saveAsSmartSet = () => {
  // 功能待开发
};
</script>

<template>
  <div class="advanced-filter" @click.stop>
    <div class="advanced-filter__container">
      <!-- 匹配模式选择 -->
      <div class="advanced-filter__match-mode">
                <span class="advanced-filter__match-label">匹配</span>
        <select v-model="matchMode" class="advanced-filter__match-select">
          <option :value="true">所有</option>
          <option :value="false">任一</option>
        </select>
        <span class="advanced-filter__match-label">过滤条件:</span>
      </div>

      <!-- 过滤条件 -->
      <div class="advanced-filter__conditions">
        <!-- 分类过滤 -->
        <div class="filter-condition">
          <div class="filter-condition__label">
            <span class="filter-condition__icon">📂</span>
            <span class="filter-condition__name">分类</span>
          </div>
          <select v-model="categoryOperator" class="filter-condition__operator">
            <option value="equals">等于</option>
            <option value="not_equals">不等于</option>
          </select>
          <select v-model="categoryValue" class="filter-condition__value">
            <option :value="0">全部分类</option>
            <option
                v-for="category in questionBank.FilterCategoryList"
                :key="category.id"
                :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>

        <!-- 难度过滤 -->
        <div class="filter-condition">
          <div class="filter-condition__label">
            <span class="filter-condition__icon">⭐</span>
            <span class="filter-condition__name">难度</span>
          </div>
          <select v-model="difficultyOperator" class="filter-condition__operator">
            <option value="equals">等于</option>
            <option value="not_equals">不等于</option>
          </select>
          <select v-model="difficultyValue" class="filter-condition__value">
            <option :value="0">全部难度</option>
            <option :value="1">简单</option>
            <option :value="2">中等</option>
            <option :value="3">困难</option>
          </select>
        </div>

        <!-- 题型过滤 -->
        <div class="filter-condition">
          <div class="filter-condition__label">
            <span class="filter-condition__icon">📝</span>
            <span class="filter-condition__name">题型</span>
          </div>
          <select v-model="typeOperator" class="filter-condition__operator">
            <option value="equals">等于</option>
            <option value="not_equals">不等于</option>
          </select>
          <select v-model="typeValue" class="filter-condition__value">
            <option :value="0">全部类型</option>
            <option :value="1">单选</option>
            <option :value="2">多选</option>
            <option :value="3">判断</option>
            <option :value="4">简答</option>
          </select>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="advanced-filter__actions">
        <button class="advanced-filter__save-btn advanced-filter__save-btn--disabled" disabled>
          <el-icon>
            <Star/>
          </el-icon>
          保存为智能题单
        </button>
        <button class="advanced-filter__reset-btn" @click="resetFilters">
          <el-icon>
            <Refresh/>
          </el-icon>
          重置
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 高级过滤器主容器 */
.advanced-filter {
  position: absolute;
  top: -8px;
  left: calc(100% + 12px);
  background: white;
  border-radius: 12px;
  padding: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  z-index: 1000;
  width: 480px;
  border: 1px solid #e5e7eb;
}

/* 箭头指示器 */
.advanced-filter::before {
  content: '';
  position: absolute;
  top: 16px;
  left: -8px;
  width: 0;
  height: 0;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
  border-right: 8px solid white;
}

.advanced-filter::after {
  content: '';
  position: absolute;
  top: 14px;
  left: -10px;
  width: 0;
  height: 0;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-right: 10px solid #e5e7eb;
  z-index: -1;
}

/* 内容容器 */
.advanced-filter__container {
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

/* 匹配模式区域 */
.advanced-filter__match-mode {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.advanced-filter__match-select {
  width: 80px;
}

.advanced-filter__match-label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

/* 过滤条件容器 */
.advanced-filter__conditions {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

/* 单个过滤条件 */
.filter-condition {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 过滤条件标签区域 */
.filter-condition__label {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 80px;
}

.filter-condition__icon {
  font-size: 16px;
}

.filter-condition__name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

/* 过滤条件选择器 */
.filter-condition__operator {
  width: 100px;
}

.filter-condition__value {
  width: 150px;
}

/* 原生选择框样式 */
.advanced-filter__match-select,
.filter-condition__operator,
.filter-condition__value {
  padding: 6px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  background-color: #fafafa;
  color: #333;
  font-size: 14px;
  outline: none;
  transition: all 0.2s ease;
  cursor: pointer;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-image: url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 12 8"><path fill="%23666" d="M6 8L0 2h12z"/></svg>');
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 10px;
  padding-right: 28px;
}

.advanced-filter__match-select:hover,
.filter-condition__operator:hover,
.filter-condition__value:hover {
  background-color: white;
  border-color: #999;
  background-image: url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 12 8"><path fill="%23999" d="M6 8L0 2h12z"/></svg>');
}

.advanced-filter__match-select:focus,
.filter-condition__operator:focus,
.filter-condition__value:focus {
  background-color: white;
  border-color: #666;
  box-shadow: 0 0 0 2px rgba(102, 102, 102, 0.1);
  background-image: url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 12 8"><path fill="%23666" d="M6 8L0 2h12z"/></svg>');
}

/* 选择框选项样式 */
.advanced-filter__match-select option,
.filter-condition__operator option,
.filter-condition__value option {
  padding: 8px 12px;
  background-color: white;
  color: #333;
  border: none;
  font-size: 14px;
}

.advanced-filter__match-select option:hover,
.filter-condition__operator option:hover,
.filter-condition__value option:hover {
  background-color: #f5f5f5;
}

.advanced-filter__match-select option:checked,
.filter-condition__operator option:checked,
.filter-condition__value option:checked {
  background-color: #e6f3ff;
  color: #1890ff;
}

/* 自定义滚动条样式 */
.advanced-filter__match-select::-webkit-scrollbar,
.filter-condition__operator::-webkit-scrollbar,
.filter-condition__value::-webkit-scrollbar {
  width: 6px;
}

.advanced-filter__match-select::-webkit-scrollbar-track,
.filter-condition__operator::-webkit-scrollbar-track,
.filter-condition__value::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.advanced-filter__match-select::-webkit-scrollbar-thumb,
.filter-condition__operator::-webkit-scrollbar-thumb,
.filter-condition__value::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.advanced-filter__match-select::-webkit-scrollbar-thumb:hover,
.filter-condition__operator::-webkit-scrollbar-thumb:hover,
.filter-condition__value::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 操作按钮区域 */
.advanced-filter__actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

/* 按钮基础样式 */
.advanced-filter__save-btn,
.advanced-filter__reset-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.2s;
}

/* 保存按钮样式 */
.advanced-filter__save-btn {
  background: #f3f4f6;
  color: #374151;
}

.advanced-filter__save-btn--disabled {
  background: #e5e7eb;
  color: #9ca3af;
  cursor: not-allowed;
}

/* 重置按钮样式 */
.advanced-filter__reset-btn {
  background: #f3f4f6;
  color: #374151;
}

.advanced-filter__reset-btn:hover {
  background: #e5e7eb;
}


</style>