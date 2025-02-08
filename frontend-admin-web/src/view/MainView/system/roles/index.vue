<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import SearchForm from './components/SearchForm.vue'
import RoleTable from './components/RoleTable.vue'
import RoleDialog from './components/RoleDialog.vue'
import PermissionDialog from './components/PermissionDialog.vue'
import BatchToolbar from './components/BatchToolbar.vue'
import { useRoleList } from './hooks/useRoleList'
import { useRoleDialog } from './hooks/useRoleDialog'
import type { SearchParams } from '../types'
import type { RoleInfo } from './types/role'

// 使用hooks
const {
  loading,
  roleList,
  pagination,
  fetchRoleList,
  handleSizeChange,
  handleCurrentChange,
  handleSortChange
} = useRoleList()

const {
  dialogVisible,
  dialogTitle,
  formData,
  submitLoading,
  openAddDialog,
  openEditDialog,
  handleSubmit
} = useRoleDialog()

// 权限配置相关
const permissionDialogVisible = ref(false)
const currentRoleId = ref<number>(0)
const permissionDialogReadonly = ref(false)

const handleConfigPermission = (row: RoleInfo) => {
  currentRoleId.value = row.id
  permissionDialogReadonly.value = false
  permissionDialogVisible.value = true
}

const handleViewPermissions = (row: RoleInfo) => {
  currentRoleId.value = row.id
  permissionDialogReadonly.value = true
  permissionDialogVisible.value = true
}

// 搜索条件
const searchParams = ref<SearchParams>({})

// 搜索处理
const handleSearch = (params: SearchParams) => {
  searchParams.value = params
  fetchRoleList(params)
}

// 重置搜索
const handleReset = () => {
  searchParams.value = {}
  fetchRoleList()
}

// 删除处理
const handleDeleteConfirm = async (row: RoleInfo) => {
  try {
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    fetchRoleList(searchParams.value)
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 提交处理
const handleDialogSubmit = async () => {
  const success = await handleSubmit()
  if (success) {
    fetchRoleList(searchParams.value)
    ElMessage.success('操作成功')
  }
}

onMounted(() => {
  fetchRoleList()
})
</script>

<template>
  <div class="role-container">
    <!-- 标题区域 -->
    <div class="page-header">
      <h2 class="page-title">角色管理</h2>
      <el-button type="primary" @click="openAddDialog">
        <el-icon><Plus /></el-icon>添加角色
      </el-button>
    </div>
    <!-- 搜索表单 -->
    <SearchForm
      :loading="loading"
      @search="handleSearch"
      @reset="handleReset"
    />

    <!-- 数据表格 -->
    <RoleTable
      :loading="loading"
      :data-list="roleList"
      :pagination="pagination"
      @config-permission="handleConfigPermission"
      @view-permissions="handleViewPermissions"
      @edit="openEditDialog"
      @delete="handleDeleteConfirm"
      @update:pagination="val => pagination = val"
      @sort-change="handleSortChange"
    />

    <!-- 角色弹窗 -->
    <RoleDialog
      v-model:visible="dialogVisible"
      :title="dialogTitle"
      :form-data="formData"
      :loading="submitLoading"
      @submit="handleDialogSubmit"
    />

    <!-- 权限配置弹窗 -->
    <PermissionDialog
      v-model="permissionDialogVisible"
      :role-id="currentRoleId"
      :readonly="permissionDialogReadonly"
    />
  </div>
</template>

<style scoped>
.role-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background-color: #fff;
  padding: 16px 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2f3d;
  margin: 0;
}
</style>