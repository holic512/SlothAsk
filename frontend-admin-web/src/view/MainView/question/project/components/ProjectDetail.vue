<script setup>

const visible = defineModel('visible')

const props = defineProps({
  data: {
    type: Object,
    default: () => ({})
  }
})

// 状态格式化
const formatStatus = (status) => {
  const statusMap = {
    1: { text: '正常', type: 'success' },
    0: { text: '禁用', type: 'danger' }
  }
  return statusMap[status] || { text: '未知', type: 'info' }
}

// 关闭弹窗
const handleClose = () => {
  visible.value = false
}
</script>

<template>
  <el-dialog
    title="项目详情"
    v-model="visible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="项目名称">
        {{ data.name }}
      </el-descriptions-item>

      <el-descriptions-item label="项目描述">
        {{ data.description || '暂无描述' }}
      </el-descriptions-item>

      <el-descriptions-item label="排序序号">
        {{ data.sortOrder}}
      </el-descriptions-item>

      <el-descriptions-item label="状态">
        <el-tag
          :type="formatStatus(data.status).type"
          effect="plain"
          size="small"
        >
          {{ formatStatus(data.status).text }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="分类/问题">
        <div class="count-info">
          <span class="count-item">
            分类数：{{ data.categoryCount ?? 0 }}
          </span>
          <el-divider direction="vertical" />
          <span class="count-item">
            问题数：{{ data.questionCount ?? 0 }}
          </span>
        </div>
      </el-descriptions-item>

      <el-descriptions-item label="创建时间">
        {{ data.createTime || '暂无' }}
      </el-descriptions-item>

      <el-descriptions-item label="更新时间">
        {{ data.updateTime || '暂无' }}
      </el-descriptions-item>
    </el-descriptions>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

:deep(.el-descriptions__label) {
  width: 120px;
  justify-content: flex-end;
  padding: 12px 16px;
}

:deep(.el-descriptions__content) {
  padding: 12px 16px;
}

:deep(.el-tag) {
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

:deep(.el-tag--success) {
  --el-tag-bg-color: var(--el-color-success-light-9);
  --el-tag-border-color: var(--el-color-success-light-5);
  --el-tag-text-color: var(--el-color-success);
}

:deep(.el-tag--danger) {
  --el-tag-bg-color: var(--el-color-danger-light-9);
  --el-tag-border-color: var(--el-color-danger-light-5);
  --el-tag-text-color: var(--el-color-danger);
}

:deep(.el-tag--info) {
  --el-tag-bg-color: var(--el-color-info-light-9);
  --el-tag-border-color: var(--el-color-info-light-5);
  --el-tag-text-color: var(--el-color-info);
}

.count-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.count-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--el-text-color-regular);
}

.count-item .el-icon {
  font-size: 16px;
  color: var(--el-text-color-secondary);
}

:deep(.el-divider--vertical) {
  height: 16px;
  margin: 0;
}
</style>
