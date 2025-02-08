<script setup lang="ts">
import { ref } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import type { SearchParams, AdminRole } from '../types'

defineProps<{
  loading?: boolean
  roleList: AdminRole[]
}>()

const emit = defineEmits<{
  search: [params: SearchParams]
  reset: []
}>()

const formRef = ref<FormInstance>()
const formData = ref<SearchParams>({
  username: '',
  real_name: '',
  role_id: undefined,
  status: undefined
})

// 搜索
const handleSearch = () => {
  emit('search', { ...formData.value })
}

// 重置
const handleReset = () => {
  formRef.value?.resetFields()
  formData.value = {
    username: '',
    real_name: '',
    role_id: undefined,
    status: undefined
  }
  emit('reset')
}
</script>

<template>
  <div class="search-bar">
    <el-card shadow="never">
      <el-form
        ref="formRef"
        :model="formData"
        :inline="true"
        class="search-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        
        <el-form-item label="真实姓名" prop="real_name">
          <el-input
            v-model="formData.real_name"
            placeholder="请输入真实姓名"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="角色" prop="role_id">
          <el-select
            v-model="formData.role_id"
            placeholder="请选择角色"
            clearable
          >
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            >
              <span>{{ role.name }}</span>
              <span class="role-description">{{ role.description }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select
            v-model="formData.status"
            placeholder="请选择状态"
            clearable
          >
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleSearch"
          >
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.search-bar {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

:deep(.el-input),
:deep(.el-select) {
  width: 220px;
}

.role-description {
  margin-left: 8px;
  color: #999;
  font-size: 13px;
}

:deep(.el-form-item) {
  margin-bottom: 0;
  margin-right: 16px;
}

:deep(.el-form-item__content) {
  margin-left: 0 !important;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style> 