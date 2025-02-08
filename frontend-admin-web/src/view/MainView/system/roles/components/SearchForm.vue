<script setup lang="ts">
import { ref } from 'vue'
import type { SearchParams } from '../../types'

const props = defineProps<{
  loading: boolean
}>()

const emit = defineEmits<{
  search: [params: SearchParams]
  reset: []
}>()

const formData = ref<SearchParams>({
  name: '',
  status: undefined
})

const handleSearch = () => {
  emit('search', { ...formData.value })
}

const handleReset = () => {
  formData.value = {
    name: '',
    status: undefined
  }
  emit('reset')
}
</script>

<template>
  <el-card class="search-form">
    <el-form :model="formData" inline class="form-content">
      <el-form-item label="角色名称">
        <el-input
          v-model="formData.name"
          placeholder="请输入角色名称"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="formData.status"
          placeholder="请选择状态"
          clearable
        >
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleSearch"
        >
          搜索
        </el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style scoped>
.search-form {
  margin-bottom: 20px;
  :deep(.el-card__body) {
    padding: 18px 20px;
  }
}

.form-content {
  :deep(.el-form-item) {
    margin-bottom: 0;
    margin-right: 18px;
  }

  :deep(.el-form-item__label) {
    font-weight: normal;
  }

  :deep(.el-input),
  :deep(.el-select) {
    width: 200px;
  }
}

:deep(.el-button) {
  padding: 8px 20px;
  &:not(:last-child) {
    margin-right: 10px;
  }
}
</style> 