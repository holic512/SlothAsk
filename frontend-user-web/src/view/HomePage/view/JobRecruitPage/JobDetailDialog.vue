<template>
  <el-dialog
    v-model="visible"
    title="岗位详情"
    width="600px"
    :before-close="handleClose"
    class="job-detail-dialog"
  >
    <div v-if="jobData" class="job-detail-content">
      <!-- 基本信息 -->
      <div class="detail-section">
        <h3 class="section-title">基本信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <label>公司名称：</label>
            <span class="company-name">
              <el-icon class="company-icon"><OfficeBuilding /></el-icon>
              {{ jobData.companyName }}
            </span>
          </div>
          <div class="info-item">
            <label>岗位名称：</label>
            <span class="job-name">{{ jobData.jobName }}</span>
          </div>
          <div class="info-item">
            <label>岗位类型：</label>
            <span class="job-type">{{ jobData.jobType }}</span>
          </div>
          <div class="info-item">
            <label>工作地点：</label>
            <span class="location">
              <el-icon class="location-icon"><Location /></el-icon>
              {{ jobData.location }}
            </span>
          </div>
          <div class="info-item">
            <label>薪资范围：</label>
            <span class="salary-range">{{ jobData.salaryRange }}</span>
          </div>
          <div class="info-item">
            <label>内推码：</label>
            <span class="referral-code">{{ jobData.referralCode }}</span>
          </div>
        </div>
      </div>

      <!-- 岗位描述 -->
      <div v-if="jobData.jobDescription" class="detail-section">
        <h3 class="section-title">岗位描述</h3>
        <div class="job-description">
          {{ jobData.jobDescription }}
        </div>
      </div>

      <!-- 福利待遇 -->
      <div v-if="jobData.benefits && jobData.benefits.length > 0" class="detail-section">
        <h3 class="section-title">福利待遇</h3>
        <div class="benefits-list">
          <el-tag
            v-for="benefit in jobData.benefits"
            :key="benefit"
            type="success"
            class="benefit-tag"
          >
            {{ benefit }}
          </el-tag>
        </div>
      </div>

      <!-- 时间信息 -->
      <div class="detail-section">
        <h3 class="section-title">时间信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <label>发布时间：</label>
            <span>{{ formatDate(jobData.publishTime) }}</span>
          </div>
          <div class="info-item">
            <label>截止时间：</label>
            <span>{{ formatDate(jobData.endTime) }}</span>
          </div>
          <div class="info-item">
            <label>申请状态：</label>
            <el-tag :type="getStatusType(jobData.applicationStatus)">
              {{ jobData.applicationStatus }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button type="primary" @click="handleApply">
          立即申请
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import {
  ElDialog,
  ElIcon,
  ElTag,
  ElButton
} from 'element-plus'
import { Location, OfficeBuilding } from '@element-plus/icons-vue'
import type { JobItem } from './type/JobItem'
import { ApplicationStatus } from './type/JobItem'

// Props
interface Props {
  modelValue: boolean
  jobData?: JobItem | null
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: false,
  jobData: null
})

// Emits
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  apply: [job: JobItem]
}>()

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (value: boolean) => emit('update:modelValue', value)
})

// 方法
const handleClose = () => {
  visible.value = false
}

const handleApply = () => {
  if (props.jobData) {
    emit('apply', props.jobData)
    window.open(props.jobData.applyUrl, '_blank')
  }
}

const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

const getStatusType = (status: ApplicationStatus) => {
  const statusMap: Record<ApplicationStatus, string> = {
    [ApplicationStatus.PENDING_SUBMISSION]: 'info',
    [ApplicationStatus.SUBMITTED]: 'warning',
    [ApplicationStatus.PENDING_WRITTEN_TEST]: 'primary',
    [ApplicationStatus.WRITTEN_TEST_IN_PROGRESS]: 'primary',
    [ApplicationStatus.FIRST_INTERVIEW]: 'success',
    [ApplicationStatus.SECOND_INTERVIEW]: 'success',
    [ApplicationStatus.THIRD_INTERVIEW]: 'success'
  }
  return statusMap[status] || 'info'
}
</script>

<style scoped>
.job-detail-dialog :deep(.el-dialog__header) {
  background: #ffffff;
  padding: 20px 24px;
  border-bottom: 1px solid #e0e0e0;
}

.job-detail-dialog :deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.job-detail-content {
  padding: 0;
  max-height: 60vh;
  overflow-y: auto;
}

/* Element Plus 滚动条样式 */
.job-detail-content::-webkit-scrollbar {
  width: 6px;
}

.job-detail-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.job-detail-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.job-detail-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.detail-section {
  margin-bottom: 24px;
  padding: 0 24px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #409eff;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item label {
  font-weight: 500;
  color: #666;
  min-width: 80px;
  margin-right: 8px;
}

.company-name,
.location {
  display: flex;
  align-items: center;
  gap: 4px;
}

.company-icon,
.location-icon {
  color: #666;
  font-size: 14px;
}

.job-name {
  font-weight: 600;
  color: #333;
}

.salary-range {
  font-weight: 600;
  color: #e6a23c;
}

.referral-code {
  font-family: 'Courier New', monospace;
  background: #f0f2f5;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.job-description {
  line-height: 1.6;
  color: #666;
  background: #ffffff;
  padding: 16px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  border-left: 4px solid #409eff;
}

.benefits-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.benefit-tag {
  margin: 0;
}

.dialog-footer {
  padding: 16px 24px;
  border-top: 1px solid #e0e0e0;
  background: #ffffff;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .job-detail-dialog :deep(.el-dialog) {
    width: 90% !important;
    margin: 5vh auto;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>