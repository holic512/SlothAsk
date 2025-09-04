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
              <el-option :label="ApplicationStatus.PENDING_SUBMISSION" :value="ApplicationStatus.PENDING_SUBMISSION" />
              <el-option :label="ApplicationStatus.SUBMITTED" :value="ApplicationStatus.SUBMITTED" />
              <el-option :label="ApplicationStatus.PENDING_WRITTEN_TEST" :value="ApplicationStatus.PENDING_WRITTEN_TEST" />
              <el-option :label="ApplicationStatus.WRITTEN_TEST_IN_PROGRESS" :value="ApplicationStatus.WRITTEN_TEST_IN_PROGRESS" />
              <el-option :label="ApplicationStatus.FIRST_INTERVIEW" :value="ApplicationStatus.FIRST_INTERVIEW" />
              <el-option :label="ApplicationStatus.SECOND_INTERVIEW" :value="ApplicationStatus.SECOND_INTERVIEW" />
              <el-option :label="ApplicationStatus.THIRD_INTERVIEW" :value="ApplicationStatus.THIRD_INTERVIEW" />
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
              <el-option :label="JobType.CAMPUS_RECRUITMENT" :value="JobType.CAMPUS_RECRUITMENT" />
              <el-option :label="JobType.SOCIAL_RECRUITMENT" :value="JobType.SOCIAL_RECRUITMENT" />
              <el-option :label="JobType.SUMMER_INTERNSHIP" :value="JobType.SUMMER_INTERNSHIP" />
              <el-option :label="JobType.WINTER_INTERNSHIP" :value="JobType.WINTER_INTERNSHIP" />
              <el-option :label="JobType.SPRING_RECRUITMENT" :value="JobType.SPRING_RECRUITMENT" />
              <el-option :label="JobType.AUTUMN_RECRUITMENT" :value="JobType.AUTUMN_RECRUITMENT" />
              <el-option :label="JobType.DAILY_INTERNSHIP" :value="JobType.DAILY_INTERNSHIP" />
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
              <el-option label="最晚发布" :value="JobSortBy.LATEST_PUBLISHED" />
              <el-option label="最早发布" :value="JobSortBy.EARLIEST_PUBLISHED" />
              <el-option label="最晚结束" :value="JobSortBy.LATEST_END" />
              <el-option label="最早结束" :value="JobSortBy.EARLIEST_END" />
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
import { ref, reactive } from 'vue'
import { 
  ElInput, 
  ElSelect, 
  ElOption, 
  ElButton, 
  ElIcon 
} from 'element-plus'
import { Search, RefreshLeft } from '@element-plus/icons-vue'
import type { JobSearchFilter } from './type/JobItem'
import { JobSortBy, JobType, ApplicationStatus } from './type/JobItem'

// 定义 emits
const emit = defineEmits<{
  search: [keyword: string]
  filter: [filters: JobSearchFilter & { sortBy?: JobSortBy }]
  reset: []
}>()

// 响应式数据
const searchKeyword = ref('')
const sortBy = ref<JobSortBy>(JobSortBy.LATEST_PUBLISHED)
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