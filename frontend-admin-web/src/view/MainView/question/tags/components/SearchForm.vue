<script setup lang="ts">
import {Delete, Plus, Search, Sort, ArrowDown} from "@element-plus/icons-vue"

// 使用 defineModel 来定义双向绑定的属性
const selectedCount = defineModel('selectedCount', {default: 0})
const keyword = defineModel('keyword', {default: ''})

interface Emits {
  (e: 'add'): void

  (e: 'search', keyword: string): void

  (e: 'batchDelete'): void

  (e: 'sort', order: 'asc' | 'desc'): void
}

const emit = defineEmits<Emits>()

const handleSearch = () => {
  emit('search', keyword.value)
}

const handleAdd = () => {
  emit('add')
}

const handleBatchDelete = () => {
  emit('batchDelete')
}

</script>

<template>
  <div class="search-area">
        <el-input
            v-model="keyword"
            placeholder="请输入标签名称"
            clearable
            style="width: 300px"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch">
              搜索
            </el-button>
          </template>
        </el-input>

      <div class="operation-buttons">
        <el-button
            type="danger"
            :icon="Delete"
            :disabled="selectedCount === 0"
            @click="handleBatchDelete"
        >
          删除选中({{ selectedCount }})
        </el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增标签</el-button>
      </div>
  </div>
</template>

<style scoped>
.search-area {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
}

.search-form {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-item {
  margin-right: 0;
}

.operation-buttons {
  display: flex;
  gap: 8px;
}

.operation-buttons .el-button {
  min-width: 120px;
}

.operation-buttons .el-dropdown {
  margin-right: 8px;
}

.operation-buttons .el-dropdown .el-button {
  display: flex;
  align-items: center;
}
</style> 