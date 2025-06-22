<script lang="ts" setup>
import {computed, ref} from 'vue';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import {Close, Delete, Search} from "@element-plus/icons-vue";

// 定义 props
interface Props {
  visible: boolean;
  selectedTags: Set<number>;
}

const props = defineProps<Props>();

// 定义 emits
const emit = defineEmits<{
  tagSelect: (tagId: number) => void;
  clearTags: () => void;
}>();

// 获取题库实体类
const questionBank = useQuestionBankStore();

// 标签搜索文本
const tagSearchText = ref('');

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

// 防抖处理标签搜索
const debouncedTagSearch = debounce((value: string) => {
  tagSearchText.value = value;
}, 300);

const handleTagSearchInput = (value: string) => {
  // 立即更新UI显示
  tagSearchText.value = value;
  // 延迟过滤标签列表
  debouncedTagSearch(value);
};

// 过滤标签列表
const filteredTags = computed(() => {
  if (!tagSearchText.value) return questionBank.FilterTagList;
  return questionBank.FilterTagList.filter(tag =>
      tag.name.toLowerCase().includes(tagSearchText.value.toLowerCase())
  );
});

// 选择标签
const handleTagSelect = (tagId: number) => {
  emit('tagSelect', tagId);
};

// 清空标签
const clearTags = () => {
  emit('clearTags');
};
</script>

<template>
  <!-- 标签筛选部分 -->
  <div v-if="visible" class="tag-filter-container">
    <div class="tag-search">
      <div class="search-row">
        <div class="search-input-wrapper">
          <el-icon class="search-icon">
            <Search/>
          </el-icon>
          <input v-model="tagSearchText"
                 class="tag-search-input"
                 placeholder="搜索标签..."
                 type="text"
                 @input="handleTagSearchInput($event.target.value)">
          <el-icon v-if="tagSearchText" class="clear-icon" @click="tagSearchText = ''">
            <Close/>
          </el-icon>
        </div>
        
        <button class="delete-button" title="清空所有标签" @click="clearTags">
          <el-icon>
            <Delete/>
          </el-icon>
        </button>
      </div>
    </div>
    <div class="tag-filter">
      <span v-for="tag in filteredTags" :key="tag.id"
            :class="['filter-tag', { active: selectedTags.has(tag.id) }]"
            @click="handleTagSelect(tag.id)">
        {{ tag.name }}
      </span>
    </div>
  </div>
</template>

<style scoped>
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

.search-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-input-wrapper {
  flex: 1;
  position: relative;
  max-width: 280px;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  font-size: 16px;
  z-index: 1;
}

.clear-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  cursor: pointer;
  font-size: 16px;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.2s;
  z-index: 1;
}

.clear-icon:hover {
  background: #f0f0f0;
  color: #666;
}

.tag-search-input {
  width: 100%;
  height: 36px;
  padding: 0 40px 0 40px;
  border: 1px solid #e8e8e8;
  border-radius: 18px;
  font-size: 14px;
  background: white;
  transition: all 0.2s;
  box-sizing: border-box;
}

.tag-search-input:focus {
  outline: none;
  border-color: #666;
  box-shadow: 0 0 0 2px rgba(102, 102, 102, 0.1);
}

.tag-search-input::placeholder {
  color: #999;
}

.delete-button {
  height: 36px;
  width: 36px;
  border: none;
  border-radius: 50%;
  background: #f5f5f5;
  color: #666;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  flex-shrink: 0;
}

.delete-button:hover {
  background: #e8e8e8;
  color: #333;
}

.delete-button:active {
  transform: scale(0.95);
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
</style>