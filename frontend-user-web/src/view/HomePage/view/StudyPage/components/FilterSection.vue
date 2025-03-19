<template xmlns="http://www.w3.org/1999/html">
  <div class="filter-row">
    <div class="filter-group">
      <el-select v-model="filterCategory" class="filter-select" @change="updateFilterCategory(filterCategory)">
        <el-option :value="0" label="全部分类"></el-option>
        <el-option v-for="category in allCategories" :key="category.id" :value="category.id" :label="category.name"></el-option>
      </el-select>

      <el-select v-model="filterType" class="filter-select" @change="updateFilterType(filterType)">
        <el-option :value="0" label="全部类型"></el-option>
        <el-option :value="1" label="单选"></el-option>
        <el-option :value="2" label="多选"></el-option>
        <el-option :value="3" label="判断"></el-option>
        <el-option :value="4" label="简答"></el-option>
      </el-select>

      <el-select v-model="filterDifficulty" class="filter-select" @change="updateFilterDifficulty(filterDifficulty)">
        <el-option :value="0" label="全部难度"></el-option>
        <el-option :value="1" label="简单"></el-option>
        <el-option :value="2" label="中等"></el-option>
        <el-option :value="3" label="困难"></el-option>
      </el-select>

      <el-button  class="tag-button" :class="{ active: showTagFilter }" @click="toggleTagFilter">
        标签
        <span v-if="filterTag" class="selected-tag">{{ filterTag }}</span>
      </el-button>
    </div>

    <div class="search-box">
      <el-input prefix-icon="search" v-model="searchText" @input="updateSearchText(searchText)" placeholder="搜索题目ID或标题..." class="search-input" />
    </div>
  </div>
  <!-- 标签筛选部分 -->
  <div v-if="showTagFilter" class="tag-filter-container">
    <div class="tag-search">
      <div class="search-input-wrapper">
        <el-icon class="search-icon">
          <Search />
        </el-icon>
        <input type="text" v-model="tagSearchText" placeholder="搜索标签..." class="tag-search-input">
        <el-icon v-if="tagSearchText" class="clear-icon" @click="tagSearchText = ''">
          <Close/>
        </el-icon>
      </div>
    </div>
    <div class="tag-filter">
      <span v-for="tag in filteredTags" :key="tag" :class="['filter-tag', { active: filterTag === tag }]" @click="handleTagSelect(tag)">
        {{ tag }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, defineEmits } from 'vue';
import { useQuestionBankStore } from '@/view/HomePage/view/store/QuestionBank';
import {Close} from "@element-plus/icons-vue";


const questionBank = useQuestionBankStore();
const allCategories = computed(() => questionBank.getAllCategories);

const searchText = ref('');
const filterCategory = ref<number>(0);
const filterTag = ref('');
const filterType = ref<number>(0);
const filterDifficulty = ref<number>(0);
const showTagFilter = ref(false);
const tagSearchText = ref('');

const emit = defineEmits([
  'update:searchText',
  'update:filterCategory',
  'update:filterTag',
  'update:filterType',
  'update:filterDifficulty'
]);
const availableTags = computed(() => {
  const tagSet = new Set<string>();
  questionBank.questions.forEach(question => {
    question.tags.split(',').forEach(tag => tagSet.add(tag.trim()));
  });
  return Array.from(tagSet).sort();
});
const filteredTags = computed(() => {
  if (!tagSearchText.value) return availableTags.value;
  const searchTextLower = tagSearchText.value.toLowerCase();
  return availableTags.value.filter(tag => tag.toLowerCase().includes(searchTextLower));
});

const toggleTagFilter = () => {
  showTagFilter.value = !showTagFilter.value;
};

const handleTagSelect = (tag: string) => {
  filterTag.value = filterTag.value === tag ? '' : tag;
};




// 更新筛选条件
const updateSearchText = (text: string) => {
  emit('update:searchText', text);
};

const updateFilterCategory = (category: number) => {
  emit('update:filterCategory', category);
};

const updateFilterTag = (tag: string) => {
  emit('update:filterTag', tag);
};

const updateFilterType = (type: number) => {
  emit('update:filterType', type);
};

const updateFilterDifficulty = (difficulty: number) => {
  emit('update:filterDifficulty', difficulty);
};
</script>

<style scoped>
.filter-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 16px 0;
  gap: 16px;
}

.filter-group {
  display: flex;
  gap: 12px;
}

.search-box {
  flex-grow: 1;
  max-width: 400px;
  text-align: right;
}

.search-input {
  width: 100%;
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.filter-select {
  width: 150px;
  background: #f5f5f5;
  border: none;
}

.tag-button {
  width: 150px;
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  justify-content: flex-start;
}

.tag-button.active {
  background: #f5f5f5;
}

.selected-tag {
  padding: 2px 8px;
  background: #f0f0f0;
  border-radius: 12px;
  font-size: 12px;
  color: #666;
}



::v-deep(.el-select) {
  width: 150px;
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
</style>
