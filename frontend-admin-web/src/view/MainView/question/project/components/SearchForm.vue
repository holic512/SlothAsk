<script setup>
import {Delete, Plus, Search, Sort, ArrowDown} from "@element-plus/icons-vue";

const selectedCount = defineModel('selectedCount', {
  default: 0
})

const keyword = defineModel('keyword', {
  default: ''
})

const emit = defineEmits(['add', 'search', 'batchDelete', 'sort'])

const handleSearch = () => {
  emit('search', keyword.value)
}

const handleSort = (order) => {
  emit('sort', order)
}

const handleBatchDelete = () => {
  emit('batchDelete')
}

const handleAdd = () => {
  emit('add')
}
</script>

<template>
  <div class="search-area">

    <el-input
        v-model="keyword"
        placeholder="请输入项目名称"
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
      <el-dropdown @command="handleSort">
        <el-button :icon="Sort">
          排序
          <el-icon class="el-icon--right">
            <arrow-down/>
          </el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="asc">正序（权重从小到大）</el-dropdown-item>
            <el-dropdown-item command="desc">倒序（权重从大到小）</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button
          type="danger"
          :icon="Delete"
          :disabled="selectedCount === 0"
          @click="handleBatchDelete"
      >
        删除选中({{ selectedCount }})
      </el-button>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增项目</el-button>
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