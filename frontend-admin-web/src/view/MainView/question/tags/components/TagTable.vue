<script setup lang="ts">
import {ref, onMounted, computed} from 'vue'
import {Edit, Delete, QuestionFilled} from '@element-plus/icons-vue'
import type {TagCategory} from '../types/TagCategoryType'
import {ElMessageBox} from 'element-plus'
import {useTagStore} from "@/view/MainView/question/tags/pinia/tags";

interface Emits {
  (e: 'edit', row: TagCategory): void

  (e: 'delete', row: TagCategory): void

  (e: 'selection-change', rows: TagCategory[]): void
}

// 定义store
const tagStore = useTagStore()


const data = defineModel<TagCategory[]>()
const emit = defineEmits<Emits>()

// 表格高度，自适应屏幕
const tableHeight = ref('calc(100vh - 240px)')

// 计算属性：缓存所有项目名称和描述
const projectNames = computed(() => {
  const names: Record<number, string> = {}
  tagStore.projectList.forEach(project => {
    names[project.id] = project.name || '未知项目'
  })
  return names
})

const projectDescriptions = computed(() => {
  const descriptions: Record<number, string> = {}
  tagStore.projectList.forEach(project => {
    descriptions[project.id] = project.description || ''
  })
  return descriptions
})

// 格式化状态显示
const formatStatus = (status: number) => {
  const statusMap = {
    1: {text: '正常', type: 'success', effect: 'plain'},
    0: {text: '禁用', type: 'danger', effect: 'plain'}
  }
  return statusMap[status as keyof typeof statusMap] || {text: '未知', type: 'info', effect: 'plain'}
}

// 格式化时间显示
const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 处理删除操作
const handleDelete = (row: TagCategory) => {
  emit('delete', row)
}
</script>

<template>
  <el-table
      :data="data"
      style="width: 100%"
      border
      stripe
      :height="tableHeight"
      highlight-current-row
      @selection-change="rows => emit('selection-change', rows)"
  >
    <el-table-column type="selection" width="50" fixed/>
    <el-table-column prop="id" label="ID" width="80" sortable/>
    <el-table-column prop="name" label="标签名称" min-width="120" show-overflow-tooltip/>
    <el-table-column prop="description" label="标签描述" min-width="200" show-overflow-tooltip/>
    <el-table-column prop="project_id" label="所属项目" width="160" sortable>
      <template #default="{ row }">
        <div style="display: flex;align-items: center;gap: 4px">
          <!-- 使用计算属性来缓存项目名称和描述 -->
          <span>{{ projectNames[row.projectId] }}</span>
          <el-tooltip
              :content="projectDescriptions[row.projectId]"
              placement="right"
              :show-after="500"
              v-if="projectDescriptions[row.projectId]"
          >
            <el-icon class="info-icon">
              <QuestionFilled/>
            </el-icon>
          </el-tooltip>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="sortOrder" label="权重" width="80" sortable/>

    <!-- 状态列 -->
    <el-table-column label="状态" width="90" align="center">
      <template #default="{ row }">
        <el-tooltip
            :content="row.status === 1 ? '点击禁用' : '点击启用'"
            placement="top"
        >
          <el-tag
              :type="formatStatus(row.status).type"
              :effect="formatStatus(row.status).effect"
              size="small"
              class="status-tag"
              @click="emit('edit', { ...row, status: row.status === 1 ? 0 : 1 })"
              style="cursor: pointer"
          >
            {{ formatStatus(row.status).text }}
          </el-tag>
        </el-tooltip>
      </template>
    </el-table-column>

    <!-- 时间信息列 -->
    <el-table-column label="更新时间" width="180">
      <template #default="{ row }">
        {{ formatTime(row.updateTime) }}
      </template>
    </el-table-column>

    <!-- 操作列 -->
    <el-table-column label="操作" width="120" fixed="right">
      <template #default="{ row }">
        <div class="operation-buttons">
          <el-button
              link
              type="primary"
              :icon="Edit"
              @click="emit('edit', row)"
          >
          </el-button>
          <el-button
              link
              type="danger"
              :icon="Delete"
              @click="handleDelete(row)"
          >
          </el-button>
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
.operation-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
}

:deep(.status-tag) {
  min-width: 60px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  height: 24px;
  padding: 0 8px;
  font-size: 12px;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.3s;
}

:deep(.status-tag:hover) {
  transform: scale(1.05);
}

:deep(.el-table) {
  --el-table-border-color: var(--el-border-color-lighter);
  --el-table-header-bg-color: var(--el-fill-color-light);
  --el-table-row-hover-bg-color: var(--el-fill-color-lighter);
}

:deep(.el-button.is-link) {
  padding: 2px 4px;
}

:deep(.el-button.is-link:hover) {
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
}
</style> 