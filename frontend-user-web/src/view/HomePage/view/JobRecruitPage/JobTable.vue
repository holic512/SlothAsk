<template>
  <div class="job-table-section">
    <div class="container">

      <div class="table-container">
        <el-table 
          :data="paginatedJobs" 
          style="width: 100%"
          :header-cell-style="{ background: '#f8f9fa', color: '#333' }"
          :row-class-name="getRowClassName"
          stripe
          border
          v-loading="loading"
          element-loading-text="加载中..."
        >
          <el-table-column 
            prop="publishTime" 
            label="发布时间" 
            width="100"
            :formatter="formatDateColumn"
          />
          
          <el-table-column 
            prop="companyName" 
            label="公司名称"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div class="company-name clickable" @click="copyToClipboard(row.companyName, '公司名称')">
                <el-icon class="company-icon"><OfficeBuilding /></el-icon>
                {{ row.companyName }}
                <el-icon class="copy-icon"><DocumentCopy /></el-icon>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="jobName" 
            label="岗位" 
            width="230"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <span class="job-name-text">{{ row.jobName }}</span>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="location" 
            label="工作地点" 
            width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div v-if="row.location && row.location.trim() !== ''" class="location-with-icon">
                <el-icon class="location-icon"><Location /></el-icon>
                <span class="location-badge">{{ row.location }}</span>
              </div>
            </template>
          </el-table-column>
          

          
          <el-table-column 
            prop="referralCode" 
            label="内推码" 
            width="100"
          >
            <template #default="{ row }">
              <span v-if="row.referralCode && row.referralCode.trim() !== ''" class="referral-code clickable" @click="copyToClipboard(row.referralCode, '内推码')">
                <strong>{{ row.referralCode }}</strong>
                <el-icon class="copy-icon"><DocumentCopy /></el-icon>
              </span>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="endTime" 
            label="结束时间" 
            width="120"
            :formatter="formatDateColumn"
          >
            <template #default="{ row }">
              <span :class="['end-time', { 'expired': isExpired(row.endTime) }]">
                {{ formatDate(row.endTime) }}
              </span>
            </template>
          </el-table-column>
          
          <el-table-column 
            prop="applicationStatus" 
            label="求职进度" 
            width="130"
          >
            <template #default="{ row }">
              <el-select 
                v-model="row.applicationStatus" 
                @change="handleUpdateApplicationStatus(row)"
                class="status-select"
                size="small"
                :disabled="statusUpdating"
              >
                <el-option label="待投递" value="待投递" />
                <el-option label="投递中" value="投递中" />
                <el-option label="待笔试" value="待笔试" />
                <el-option label="笔试中" value="笔试中" />
                <el-option label="一面" value="一面" />
                <el-option label="二面" value="二面" />
                <el-option label="三面" value="三面" />
              </el-select>
            </template>
          </el-table-column>
          
          <el-table-column 
            label="操作" 
            width="200"
            fixed="right"
          >
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button 
                  type="info" 
                  size="small"
                  @click="handleViewDetail(row)"
                >
                  查看详情
                </el-button>
                <el-button 
                  type="primary" 
                  size="small"
                  @click="handleApply(row.applyUrl)"
                >
                  点击投递
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 空状态 -->
        <el-empty v-if="jobs.length === 0" description="暂无职位信息" />
      </div>

      <!-- 岗位详情弹窗 -->
      <JobDetailDialog
        v-model="showDetailDialog"
        :job-data="selectedJob"
        @apply="handleJobApply"
      />

      <!-- 分页组件 -->
      <div v-if="totalJobs > 0" class="pagination-wrapper">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="totalJobs"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlePageSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, ref} from 'vue'
import {
  ElButton,
  ElEmpty,
  ElIcon,
  ElMessage,
  ElOption,
  ElPagination,
  ElSelect,
  ElTable,
  ElTableColumn
} from 'element-plus'
import {DocumentCopy, Location, OfficeBuilding} from '@element-plus/icons-vue'
import type {JobItem} from './type/JobItem'
import {updateApplicationStatus} from './service'
import JobDetailDialog from './JobDetailDialog.vue'

const props = withDefaults(defineProps<{
  jobs: JobItem[]
  loading?: boolean
  pagination?: {
    page: number
    pageSize: number
    total: number
    totalPages: number
  }
}>(), {
  jobs: () => [],
  loading: false,
  pagination: () => ({ page: 1, pageSize: 10, total: 0, totalPages: 0 })
})

// 定义 emits
const emit = defineEmits<{
  statusUpdate: [job: JobItem]
  pageChange: [page: number]
  pageSizeChange: [size: number]
}>()

// 响应式数据
const showDetailDialog = ref(false)
const selectedJob = ref<JobItem | null>(null)
const statusUpdating = ref(false)

// 计算属性
const currentPage = computed(() => props.pagination?.page || 1)
const pageSize = computed(() => props.pagination?.pageSize || 10)
const totalJobs = computed(() => props.pagination?.total || 0)
const totalPages = computed(() => props.pagination?.totalPages || 0)

// 直接使用传入的jobs数据，不需要分页处理（后端已处理）
const paginatedJobs = computed(() => props.jobs)

// 方法

const handlePageSizeChange = (size: number) => {
  emit('pageSizeChange', size)
}

const handleCurrentChange = (page: number) => {
  emit('pageChange', page)
}

const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

const formatDateColumn = (row: JobItem, column: any, cellValue: string) => {
  return formatDate(cellValue)
}

const isExpired = (endTime: string) => {
  if (!endTime) return false
  const endDate = new Date(endTime)
  const currentDate = new Date()
  return currentDate > endDate
}

const handleApply = (url: string) => {
  window.open(url, '_blank')
}

const handleUpdateApplicationStatus = async (job: JobItem) => {
  try {
    statusUpdating.value = true
    
    // 调用service接口更新申请状态
    const success = await updateApplicationStatus(job.id, job.applicationStatus)
    
    if (success) {
      ElMessage.success('申请状态更新成功')
      // 通知父组件状态已更新
      emit('statusUpdate', job)
    } else {
      ElMessage.error('申请状态更新失败')
    }
  } catch (error: any) {
    console.error('更新申请状态失败:', error)
    ElMessage.error(error.message || '申请状态更新失败，请稍后重试')
  } finally {
    statusUpdating.value = false
  }
}

const handleViewDetail = (job: JobItem) => {
  selectedJob.value = job
  showDetailDialog.value = true
}

const handleJobApply = (job: JobItem) => {
  // 可以在这里添加额外的申请逻辑
  console.log('申请岗位:', job.jobName)
}

const copyToClipboard = async (text: string, type: string) => {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success(`${type}已复制到剪贴板`)
  } catch (err) {
    // 降级方案
    try {
      const textArea = document.createElement('textarea')
      textArea.value = text
      document.body.appendChild(textArea)
      textArea.select()
      document.execCommand('copy')
      document.body.removeChild(textArea)
      ElMessage.success(`${type}已复制到剪贴板`)
    } catch (fallbackErr) {
      ElMessage.error('复制失败，请手动复制')
    }
  }
}

// 根据求职状态设置行样式
const getRowClassName = ({ row }: { row: JobItem }) => {
  // 检查是否过期
  if (isExpired(row.endTime)) {
    return 'row-expired' // 过期状态 - 红色删除线
  }
  
  const status = row.applicationStatus
  
  switch (status) {
    case '待投递':
      return 'row-status-info' // 待投递 - 信息状态
    case '投递中':
      return 'row-status-warning' // 投递中 - 警告状态
    case '待笔试':
    case '笔试中':
      return 'row-status-primary' // 笔试阶段 - 主要状态
    case '一面':
    case '二面':
    case '三面':
      return 'row-status-success' // 面试阶段 - 成功状态
    default:
      return ''
  }
}


</script>

<style scoped>
.job-table-section {
  background: white;
  padding: 0;
  margin: 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e0e0e0;
}

.table-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.table-controls {
  display: flex;
  align-items: center;
  gap: 20px;
}

.sort-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.sort-controls label {
  font-weight: 500;
  color: #666;
}

.sort-select {
  min-width: 140px;
}

.sort-order-btn {
  margin-left: 8px;
}

.view-controls {
  color: #666;
  font-size: 14px;
}

.table-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Element UI Table 样式定制 */
:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background: #f8f9fa !important;
  color: #333 !important;
  font-weight: 600;
}

:deep(.el-table td) {
  padding: 12px 8px;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background: #fafafa;
}

:deep(.el-table__body tr:hover > td) {
  background-color: #f5f7fa !important;
}

/* 公司名称样式 */
.company-name {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 4px;
  border-radius: 4px;
}

.company-name:hover {
  background-color: #f0f9ff;
  color: #1976d2;
}

.company-icon {
  color: #666;
  font-size: 14px;
}

.copy-icon {
  color: #999;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.company-name:hover .copy-icon {
  opacity: 1;
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 6px;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: flex-start;
}

.action-buttons .el-button {
  margin: 0;
  flex-shrink: 0;
  white-space: nowrap;
}

/* 岗位名称样式 */
.job-name-text {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
}

/* 地点样式 */
.location-with-icon {
  display: flex;
  align-items: center;
  gap: 6px;
}

.location-icon {
  color: #666;
  font-size: 14px;
}

.location-badge {
  background: #e3f2fd;
  color: #1976d2;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

/* 薪资样式 */
.salary {
  color: #e91e63;
  font-weight: 600;
}

/* 内推码样式 */
.referral-code {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: #666;
  display: flex;
  align-items: center;

  gap: 4px;
}

.referral-code:hover {
  background: #e3f2fd;
  color: #1976d2;
}

.referral-code:hover .copy-icon {
  opacity: 1;
}

.referral-code .copy-icon {
  opacity: 0;
  transition: opacity 0.2s ease;
}

/* 通用可点击样式 */
.clickable {
  user-select: none;
}

/* 结束时间样式 */
.end-time {
  color: #666;
  font-size: 13px;
}

.end-time.expired {
  color: #f56c6c;
  text-decoration: line-through;
  font-weight: 600;
}

/* 状态选择器样式 */
.status-select {
  width: 100%;
}

:deep(.status-select .el-input__inner) {
  font-size: 12px;
}

/* 分页样式 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 20px 0;
  border-top: 1px solid #e0e0e0;
}

:deep(.el-pagination) {
  justify-content: center;
}

/* 求职状态行样式 */
:deep(.el-table .row-status-success) {
  background-color: #f0f9ff !important;
  border-left: 4px solid #67c23a;
}


:deep(.el-table .row-status-info) {
  background-color: #f4f4f5 !important;
  border-left: 4px solid #909399;
}


:deep(.el-table .row-status-warning) {
  background-color: #fdf6ec !important;
  border-left: 4px solid #e6a23c;
}



:deep(.el-table .row-status-primary) {
  background-color: #ecf5ff !important;
  border-left: 4px solid #409eff;
}

/* 过期岗位行样式 */
:deep(.el-table .row-expired) {
  background-color: #fef0f0 !important;
  border-left: 4px solid #f56c6c;
  text-decoration: line-through;
  color: #999 !important;
}

:deep(.el-table .row-expired td) {
  text-decoration: line-through;
  color: #999 !important;
}


/* 确保条纹样式不会覆盖状态样式 */
:deep(.el-table--striped .el-table__body tr.el-table__row--striped.row-status-success td) {
  background-color: #f0f9ff !important;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped.row-status-info td) {
  background-color: #f4f4f5 !important;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped.row-status-warning td) {
  background-color: #fdf6ec !important;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped.row-status-primary td) {
  background-color: #ecf5ff !important;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped.row-expired td) {
  background-color: #fef0f0 !important;
  text-decoration: line-through;
  color: #999 !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 15px;
  }
  
  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .table-controls {
    width: 100%;
    justify-content: space-between;
  }
  
  .sort-select {
    min-width: 120px;
  }
  
  :deep(.el-table) {
    font-size: 12px;
  }
  
  :deep(.el-table td) {
    padding: 8px 6px;
  }
  

  
  .company-name {
    gap: 4px;
  }
  
  .company-icon,
  .location-icon {
    font-size: 12px;
  }
  
  .pagination-wrapper {
    padding: 15px 0;
  }
  
  :deep(.el-pagination) {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  :deep(.el-pagination .el-pagination__sizes),
  :deep(.el-pagination .el-pagination__jump) {
    margin-left: 0;
  }
}
</style>