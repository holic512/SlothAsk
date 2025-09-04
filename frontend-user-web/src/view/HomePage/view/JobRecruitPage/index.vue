<script lang="ts" setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import IntroductionSection from './IntroductionSection.vue'
import FilterSection from './FilterSection.vue'
import JobTable from './JobTable.vue'
import type {JobItem, JobListQuery, JobSearchFilter} from './type/JobItem'
import {getJobList} from './service'

// 岗位数据
const allJobs = ref<JobItem[]>([])
const loading = ref(false)
const pagination = ref({
  page: 1,
  pageSize: 20,
  total: 0,
  totalPages: 0
})

// 当前显示的职位列表
const filteredJobs = ref<JobItem[]>([...allJobs.value])

// 当前搜索关键词
// 当前筛选条件
const currentSearchKeyword = ref('')
const currentSortBy = ref<string>('最晚发布')
const currentFilters = reactive<JobSearchFilter>({
  keyword: '',
  applicationStatuses: [],
  jobTypes: []
})

// 获取岗位列表数据
const fetchJobList = async () => {
  try {
    loading.value = true
    
    const queryParams: JobListQuery = {
      page: pagination.value.page,
      pageSize: pagination.value.pageSize,
      sortBy: currentSortBy.value,
      filter: {
        keyword: currentFilters.keyword,
        applicationStatuses: currentFilters.applicationStatuses,
        jobTypes: currentFilters.jobTypes
      }
    }
    
    const response = await getJobList(queryParams)
    
    allJobs.value = response.jobs
    pagination.value = {
      page: response.currentPage,
      pageSize: response.pageSize,
      total: response.total,
      totalPages: response.totalPages
    }
    
    // 更新筛选后的职位列表
    filteredJobs.value = [...allJobs.value]
    
  } catch (error: any) {
    console.error('获取岗位列表失败:', error)
    ElMessage.error(error.message || '获取岗位列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = (keyword: string) => {
  currentSearchKeyword.value = keyword
  currentFilters.keyword = keyword
  pagination.value.page = 1 // 重置到第一页
  fetchJobList()
}

// 筛选处理
const handleFilter = (filters: JobSearchFilter & { sortBy?: string }) => {
  // 更新筛选条件
  Object.assign(currentFilters, {
    keyword: filters.keyword,
    applicationStatuses: filters.applicationStatuses,
    jobTypes: filters.jobTypes
  })
  
  // 更新排序条件
  if (filters.sortBy) {
    currentSortBy.value = filters.sortBy
  }
  
  pagination.value.page = 1 // 重置到第一页
  fetchJobList()
}

// 重置处理
const handleReset = () => {
  currentSearchKeyword.value = ''
  currentSortBy.value = '最晚发布'
  Object.assign(currentFilters, {
    keyword: '',
    applicationStatuses: [],
    jobTypes: []
  })
  pagination.value.page = 1 // 重置到第一页
  fetchJobList()
}

// 分页处理
const handlePageChange = (page: number) => {
  pagination.value.page = page
  fetchJobList()
}

const handlePageSizeChange = (size: number) => {
  pagination.value.pageSize = size
  pagination.value.page = 1 // 重置到第一页
  fetchJobList()
}

// 查看职位详情
const handleViewDetail = (job: JobItem) => {
  // 这里可以跳转到职位详情页面或打开弹窗
  console.log('查看职位详情:', job)
  alert(`查看职位详情: ${job.jobName}`)
}

// 申请职位
const handleApplyJob = (job: JobItem) => {
  // 打开申请链接
  if (job.applyUrl) {
    window.open(job.applyUrl, '_blank')
  } else {
    ElMessage.warning('该职位暂无申请链接')
  }
}

// 组件挂载时初始化数据
onMounted(() => {
  fetchJobList()
})
</script>

<template>
  <div class="job-recruit-page">
    <!-- 介绍组件 -->
    <IntroductionSection />
    
    <!-- 筛选组件 -->
    <FilterSection 
      @search="handleSearch"
      @filter="handleFilter"
      @reset="handleReset"
    />
    
    <!-- 表格展示组件 -->
    <JobTable 
      :jobs="filteredJobs"
      :loading="loading"
      :pagination="pagination"
      @view-detail="handleViewDetail"
      @apply-job="handleApplyJob"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
    />
  </div>
</template>

<style scoped>
.job-recruit-page {
  min-height: 100vh;
  background: #f5f5f5;
}

/* 全局样式重置 */
* {
  box-sizing: border-box;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .job-recruit-page {
    padding: 0;
  }
}
</style>