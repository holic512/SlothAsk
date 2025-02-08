<script setup lang="ts">
import { computed } from 'vue'
import { useCategoryStore } from "../pinia/categoryStore"

const categoryStore = useCategoryStore()

// 使用计算属性处理 drawer 显示状态
const visible = computed({
  get: () => categoryStore.drawerVisible,
  set: (value) => categoryStore.drawerVisible = value
})

// 使用计算属性获取分类数据
const categoryData = computed(() => categoryStore.currentCategory)

// 格式化状态
const formatStatus = (status: number) => {
  const statusMap = {
    1: { text: '正常', type: 'success' },
    0: { text: '禁用', type: 'danger' }
  }
  return statusMap[status] || { text: '未知', type: 'info' }
}

// 获取项目名称
const getProjectName = (projectId: number) => {
  const project = categoryStore.projectOptions.find(item => item.value === projectId)
  return project?.label || '未知项目'
}
</script>

<template>
  <el-drawer
    v-model="visible"
    title="分类详情"
    size="400px"
    destroy-on-close
  >
    <template v-if="categoryData">
      <div class="category-detail">
        <div class="profile-header">
          <el-avatar
            :size="80"
            :src="categoryData.avatarUrl"
            class="category-avatar"
          >
            {{ categoryData.name.charAt(0) }}
          </el-avatar>
          <div class="category-basic-info">
            <h3 class="name">{{ categoryData.name }}</h3>
            <p class="project-name">{{ getProjectName(categoryData.projectId) }}</p>
            <el-tag
              :type="formatStatus(categoryData.status).type"
              size="small"
            >
              {{ formatStatus(categoryData.status).text }}
            </el-tag>
          </div>
        </div>

        <div class="info-section">
          <div class="section-title">基本信息</div>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">ID</span>
              <span class="value">{{ categoryData.id }}</span>
            </div>
            <div class="info-item">
              <span class="label">排序</span>
              <span class="value">{{ categoryData.sortOrder }}</span>
            </div>
            <div class="info-item full-width">
              <span class="label">描述</span>
              <span class="value description">{{ categoryData.description || '暂无描述' }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <div class="section-title">统计信息</div>
          <div class="statistics-grid">
            <div class="stat-item">
              <div class="stat-value">{{ categoryData.questionCount || 0 }}</div>
              <div class="stat-label">题目数量</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ categoryData.viewCount || 0 }}</div>
              <div class="stat-label">访问量</div>
            </div>
          </div>
        </div>

        <div class="info-section">
          <div class="section-title">其他信息</div>
          <div class="info-grid">
            <div class="info-item full-width">
              <span class="label">创建时间</span>
              <span class="value">{{ categoryData.createTime }}</span>
            </div>
            <div class="info-item full-width">
              <span class="label">更新时间</span>
              <span class="value">{{ categoryData.updateTime }}</span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </el-drawer>
</template>

<style scoped>
.category-detail {
  padding: 0 20px;
}

.profile-header {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
  margin-bottom: 24px;
}

.category-avatar {
  border: 2px solid var(--el-border-color-lighter);
  padding: 2px;
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.category-basic-info {
  margin-left: 20px;
}

.name {
  font-size: 20px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 8px 0;
}

.project-name {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin: 4px 0 8px;
}

.info-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  margin-bottom: 16px;
  padding-left: 10px;
  border-left: 3px solid var(--el-color-primary);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item.full-width {
  grid-column: span 2;
}

.label {
  font-size: 13px;
  color: var(--el-text-color-secondary);
  margin-bottom: 4px;
}

.value {
  font-size: 14px;
  color: var(--el-text-color-primary);
}

.description {
  line-height: 1.5;
  white-space: pre-wrap;
}

.info-section .el-tag {
  margin-right: 8px;
}

:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

:deep(.el-drawer__body) {
  padding: 0;
}

.statistics-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 16px;
  background-color: var(--el-fill-color-light);
  border-radius: 8px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 500;
  color: var(--el-color-primary);
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: var(--el-text-color-secondary);
  margin-top: 4px;
}
</style> 