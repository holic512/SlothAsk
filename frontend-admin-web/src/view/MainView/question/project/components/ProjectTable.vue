<script setup>
import { ref } from 'vue'
import { View, Edit, Delete, User } from '@element-plus/icons-vue'
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
const emit = defineEmits(['view', 'edit', 'delete', 'selection-change', 'view-creator'])

// 删除确认
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确认删除该项目吗？',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    emit('delete', row)
  }).catch(() => {})
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
    <el-table-column prop="sort_order" label="排序序号" width="60" />
    <el-table-column prop="id" label="ID" width="80"  />
    <el-table-column prop="project_name" label="项目名称" min-width="120" />
    <el-table-column prop="creator_id" label="创建者ID" min-width="120" show-overflow-tooltip>
      <template #default="{ row }">
        <div class="creator-cell">
          <div class="creator-info">
            <el-tooltip content="创建者详情" placement="top">
              <el-button
                  link
                  type="primary"
                  :icon="User"
                  @click="emit('view-creator', row.creator_id)"
              />
            </el-tooltip>
            <span>{{ row.creator_id }}</span>
          </div>
        </div>
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
</style>