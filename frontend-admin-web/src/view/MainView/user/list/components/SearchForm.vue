<script setup>
import { reactive } from 'vue'
import { Search, Plus, Delete } from '@element-plus/icons-vue'

const selectedCount = defineModel('selectedCount', {
  default: 0
})

const keyword = defineModel('keyword', {
  default: ''
})

const emit = defineEmits(['add', 'search', 'batchDelete'])

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
    <el-form inline class="search-form">
      <el-form-item class="search-item">
        <el-input
          v-model="keyword"
          placeholder="请输入用户名/邮箱/手机号"
          clearable
          style="width: 300px"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch">
              搜索
            </el-button>
          </template>
        </el-input>
      </el-form-item>
      <div class="operation-buttons">
        <el-button 
          type="danger" 
          :icon="Delete" 
          :disabled="selectedCount === 0"
          @click="handleBatchDelete"
        >
          删除选中({{ selectedCount }})
        </el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
      </div>
    </el-form>
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
</style> 