<script setup>
import { ref } from 'vue'
import { View, Edit, Delete, QuestionFilled, Folder, Document } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

const data = defineModel('data')
const loading = defineModel('loading')

// 表格高度
const tableHeight = ref('calc(100vh - 240px)')

// 状态格式化
const formatStatus = (status) => {
  const statusMap = {
    1: { text: '正常', type: 'success', effect: 'plain' },
    0: { text: '禁用', type: 'danger', effect: 'plain' }
  }
  return statusMap[status] || { text: '未知', type: 'info', effect: 'plain' }
}
const emit = defineEmits(['view', 'edit', 'delete', 'selection-change'])

// 删除确认
const handleDelete = (row) => {
  emit('delete', row)
}

</script>

<template>
  <el-table
      :data="data"
      style="width: 100%"
      border
      stripe
      :loading="loading"
      :height="tableHeight"
      @selection-change="rows => emit('selection-change', rows)"
  >
    <el-table-column type="selection" width="50" fixed />

    <el-table-column prop="id" label="ID" width="80" />

    <el-table-column prop="name" label="项目名称" min-width="50" />

    <el-table-column prop="description" label="项目描述" min-width="120" show-overflow-tooltip/>

    <el-table-column label="分类/问题" width="120" align="center">
      <template #default="{ row }">
        <div class="count-info">
          <el-tooltip content="分类数量" placement="top">
            <span class="count-item">
              <el-icon><Folder /></el-icon>
              {{ row.category_count }}
            </span>
          </el-tooltip>
          <el-divider direction="vertical" />
          <el-tooltip content="问题数量" placement="top">
            <span class="count-item">
              <el-icon><Document /></el-icon>
              {{ row.question_count }}
            </span>
          </el-tooltip>
        </div>
      </template>
    </el-table-column>

    <el-table-column prop="sortOrder" label="权重" width="80">
      <template #header>
        <el-tooltip
            content="权重值越大，排序越靠前"
            placement="top"
            effect="light"
        >
          <div class="weight-header">
            权重
            <el-icon class="question-icon"><QuestionFilled /></el-icon>
          </div>
        </el-tooltip>
      </template>
    </el-table-column>

    

    <el-table-column label="状态" width="90" align="center">
      <template #default="{ row }">
        <el-tag
            :type="formatStatus(row.status).type"
            :effect="formatStatus(row.status).effect"
            size="small"
            class="status-tag"
        >
          {{ formatStatus(row.status).text }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="240" fixed="right">
      <template #default="{ row }">
        <div class="operation-buttons">
          <el-tooltip content="查看详情" placement="top">
            <el-button
                link
                type="primary"
                :icon="View"
                @click="emit('view', row)"
            />
          </el-tooltip>
          <el-tooltip content="编辑" placement="top">
            <el-button
                link
                type="primary"
                :icon="Edit"
                @click="emit('edit', row)"
            />
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button
                link
                type="danger"
                :icon="Delete"
                @click="handleDelete(row)"
            />
          </el-tooltip>
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
}

:deep(.status-tag.el-tag--success) {
  --el-tag-bg-color: var(--el-color-success-light-9);
  --el-tag-border-color: var(--el-color-success-light-5);
  --el-tag-text-color: var(--el-color-success);
}

:deep(.status-tag.el-tag--danger) {
  --el-tag-bg-color: var(--el-color-danger-light-9);
  --el-tag-border-color: var(--el-color-danger-light-5);
  --el-tag-text-color: var(--el-color-danger);
}

:deep(.status-tag.el-tag--info) {
  --el-tag-bg-color: var(--el-color-info-light-9);
  --el-tag-border-color: var(--el-color-info-light-5);
  --el-tag-text-color: var(--el-color-info);
}

:deep(.el-table__cell) {
  padding: 6px 0;
}

:deep(.el-table .cell) {
  padding: 0 8px;
}

:deep(.el-button.is-link) {
  padding: 4px;
  border-radius: 4px;
}

:deep(.el-button.is-link:hover) {
  background-color: var(--el-fill-color-light);
}

.creator-cell {
  display: flex;
  align-items: center;
}

.creator-info {
  display: flex;
  align-items: center;
  gap: 4px;  /* 按钮和ID之间的间距 */
}

.creator-info .el-button {
  padding: 2px;
}

.creator-info .el-button:hover {
  background-color: var(--el-fill-color-light);
}

.weight-header {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.question-icon {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  cursor: help;
}

.question-icon:hover {
  color: var(--el-color-primary);
}

.count-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.count-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--el-text-color-regular);
}

.count-item .el-icon {
  font-size: 14px;
}

:deep(.el-divider--vertical) {
  height: 16px;
  margin: 0;
}
</style>