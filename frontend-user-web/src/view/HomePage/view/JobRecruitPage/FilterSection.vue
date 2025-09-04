<template>
  <div class="filter-section">
    <div class="container">
      <div class="filter-content">
        <!-- 单行筛选布局 -->
        <div class="filter-row">
          <!-- 搜索框 -->
          <div class="search-box">
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索职位名称、公司名称、工作地点或关键词..."
              class="search-input"
              @keyup.enter="handleSearch"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>



          <!-- 求职进度筛选 -->
          <div class="filter-group">
            <el-select 
              v-model="filters.applicationStatuses" 
              placeholder="求职进度"
              multiple
              collapse-tags
              collapse-tags-tooltip
              class="filter-select"
              size="default"
            >
              <el-option label="待投递" value="待投递" />
              <el-option label="投递中" value="投递中" />
              <el-option label="待笔试" value="待笔试" />
              <el-option label="笔试中" value="笔试中" />
              <el-option label="一面" value="一面" />
              <el-option label="二面" value="二面" />
              <el-option label="三面" value="三面" />
            </el-select>
          </div>

          <!-- 岗位类型筛选 -->
          <div class="filter-group">
            <el-select 
              v-model="filters.jobTypes" 
              placeholder="岗位类型"
              multiple
              collapse-tags
              collapse-tags-tooltip
              class="filter-select"
              size="default"
            >
              <el-option label="校招" value="校招" />
              <el-option label="社招" value="社招" />
              <el-option label="暑期实习" value="暑期实习" />
              <el-option label="寒假实习" value="寒假实习" />
              <el-option label="春招" value="春招" />
              <el-option label="秋招" value="秋招" />
              <el-option label="日常实习" value="日常实习" />
            </el-select>
          </div>

          <!-- 时间排序 -->
          <div class="filter-group">
            <el-select 
              v-model="sortBy" 
              placeholder="时间排序"
              class="sort-select"
              size="default"
            >
              <el-option label="最晚发布" value="最晚发布" />
              <el-option label="最早发布" value="最早发布" />
              <el-option label="最晚结束" value="最晚结束" />
              <el-option label="最早结束" value="最早结束" />
            </el-select>
          </div>

          <!-- 操作按钮 -->
          <div class="filter-actions">
            <el-button 
              type="primary" 
              @click="handleFilter"
              :icon="Search"
              size="default"
            >
              筛选
            </el-button>
            <el-button 
              @click="handleReset"
              :icon="RefreshLeft"
              size="default"
            >
              重置
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref} from 'vue'
import {ElButton, ElIcon, ElInput, ElOption, ElSelect} from 'element-plus'
import {RefreshLeft, Search} from '@element-plus/icons-vue'
import type {JobSearchFilter} from './type/JobItem'
import {JobSortBy} from './type/JobItem'

// 定义 emits
const emit = defineEmits<{
  search: [keyword: string]
  filter: [filters: JobSearchFilter & { sortBy?: string }]
  reset: []
}>()

// 响应式数据
const searchKeyword = ref('')
const sortBy = ref<string>('最晚发布')
const filters = reactive<JobSearchFilter>({
  keyword: '',
  applicationStatuses: [],
  jobTypes: []
})

// 搜索处理
const handleSearch = () => {
  filters.keyword = searchKeyword.value
  handleFilter()
}

// 筛选处理
const handleFilter = () => {
  const filterData = {
    ...filters,
    keyword: searchKeyword.value,
    sortBy: sortBy.value
  }
  emit('filter', filterData)
}

// 重置处理
const handleReset = () => {
  searchKeyword.value = ''
  sortBy.value = JobSortBy.LATEST_PUBLISHED
  filters.keyword = ''
  filters.applicationStatuses = []
  filters.jobTypes = []
  emit('reset')
}
</script>

<style scoped>
.filter-section {
  background: white;
  padding: 12px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.filter-content {
  background: white;
  padding: 4px;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 280px;
}

.search-input {
  width: 100%;
}

.filter-group {
  min-width: 140px;
}

.filter-select {
  width: 100%;
}

.sort-select {
  width: 120px;
}



.filter-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

</style>