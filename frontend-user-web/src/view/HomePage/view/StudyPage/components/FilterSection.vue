<script setup lang="ts">
import {ref, computed, onMounted} from 'vue';
import {useQuestionBankStore} from '@/view/HomePage/view/StudyPage/store/QuestionBank';
import {Close, Search, Delete} from "@element-plus/icons-vue";
import {
  apiGetCategoryIdAndNameByProjectId
} from "@/view/HomePage/view/StudyPage/service/ApiGetCategoryIdAndNameByProjectId";
import {apiGetTagIdAndName} from "@/view/HomePage/view/StudyPage/service/ApiGetTagIdAndName";

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

// 过滤搜索的变量 - 直接从 store 获取
const searchText = ref('');
const filterCategory = ref<number>(0);
const filterTag = ref<Set<number>>(new Set());
const filterType = ref<number>(0);
const filterDifficulty = ref<number>(0);

// 辅助变量
const showTagFilter = ref(false);
const tagSearchText = ref('');

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

// 更新筛选条件到 store
const updateSearchText = (text: string) => {
  searchText.value = text;
  questionBank.pagination.searchText = text || null;
};

const updateFilterCategory = (category: number) => {
  filterCategory.value = category;
  questionBank.pagination.filterCategory = category === 0 ? null : category;
};

const updateFilterTag = (tags: number[]) => {
  // 直接传递数组给 store
  questionBank.pagination.filterTags = tags.length > 0 ? tags : null;
};

const updateFilterType = (type: number) => {
  filterType.value = type;
  questionBank.pagination.filterType = type === 0 ? null : type;
};

const updateFilterDifficulty = (difficulty: number) => {
  filterDifficulty.value = difficulty;
  questionBank.pagination.filterDifficulty = difficulty === 0 ? null : difficulty;
};

// 过滤标签列表
const filteredTags = computed(() => {
  if (!tagSearchText.value) return questionBank.FilterTagList;
  return questionBank.FilterTagList.filter(tag => 
    tag.name.toLowerCase().includes(tagSearchText.value.toLowerCase())
  );
});
</script>

<template xmlns="http://www.w3.org/1999/html">
  <div class="filter-row">
    <div class="filter-group">
      <el-select v-model="filterCategory" class="filter-select" @change="updateFilterCategory(filterCategory)">
        <el-option :value="0" label="全部分类"></el-option>
        <el-option v-for="category in questionBank.FilterCategoryList" :key="category.id" :value="category.id"
                   :label="category.name"></el-option>
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

      <el-button class="tag-button" :class="{ active: showTagFilter }" @click="toggleTagFilter">
        标签
        <span v-if="filterTag.size > 0" class="tag-badge">{{ filterTag.size }}</span>
      </el-button>

    </div>

    <div class="search-box">
      <el-input prefix-icon="search" v-model="searchText" @input="updateSearchText(searchText)"
                placeholder="搜索题目ID或标题..." class="search-input"/>
    </div>
  </div>

  <!-- 标签筛选部分 -->
  <div v-if="showTagFilter" class="tag-filter-container">
    <div class="tag-search">
      <div class="search-input-wrapper">

        <!--  用于搜索标签位置 -->
        <el-icon class="search-icon">
          <Search/>
        </el-icon>
        <input type="text" v-model="tagSearchText" placeholder="搜索标签..." class="tag-search-input">
        <el-icon v-if="tagSearchText" class="clear-icon" @click="tagSearchText = ''">
          <Close/>
        </el-icon>

        <!--  用于清空标签  -->
        <el-button style="height: 36px" text @click="clearTags">
          <el-icon>
            <Delete/>
          </el-icon>
        </el-button>

      </div>
    </div>
    <div class="tag-filter">
      <span v-for="tag in filteredTags" :key="tag.id"
            :class="['filter-tag', { active: filterTag.has(tag.id) }]"
            @click="handleTagSelect(tag.id)">
        {{ tag.name }}
      </span>
    </div>
  </div>
</template>


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
  right: 56px;
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
  transition: all 0.1s;
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

.tag-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  margin-left: 6px;
  font-size: 12px;
  line-height: 1;
  font-weight: 500;
  border-radius: 9px;
  background-color: #409EFF;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.active .tag-badge {
  background-color: white;
  color: #409EFF;
}
</style>
