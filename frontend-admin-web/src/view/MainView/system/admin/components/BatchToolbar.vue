<!--./admin/components/BatchToolbar.vue -->
<template>
  <div class="batch-toolbar">
    <div class="left">
      <span v-if="selectedCount" class="selected-count">

        已选择 {{ selectedCount }} 项
      </span>
      <el-button-group v-if="selectedCount">
        <el-button
          type="danger"
          :loading="loading"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
        <el-button
          type="warning"
          :loading="loading"
          @click="handleBatchDisable"
        >
          批量禁用
        </el-button>
      </el-button-group>
    </div>

    <div class="right">
      <el-button-group>
        <el-button
          :loading="loading"
          @click="handleRefresh"
        >
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleExport"
        >
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Refresh, Download } from '@element-plus/icons-vue'

defineProps<{
  selectedCount: number
  loading?: boolean
}>()

const emit = defineEmits<{
  'batch-delete': []
  'batch-disable': []
  'refresh': []
  'export': []
}>()

const handleBatchDelete = () => {
  emit('batch-delete')
}

const handleBatchDisable = () => {
  emit('batch-disable')
}

const handleRefresh = () => {
  emit('refresh')
}

const handleExport = () => {
  emit('export')
}
</script>

<style scoped>
.batch-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 12px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.selected-count {
  color: #606266;
}
</style> 