<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import SearchForm from './components/SearchForm.vue'
import AdminTable from './components/AdminTable.vue'
import AdminDialog from './components/AdminDialog.vue'
import ChangePasswordDialog from './components/ChangePasswordDialog.vue'

import { useAdminList } from './hooks/useAdminList'
import { useAdminDialog } from './hooks/useAdminDialog'
import { useAdminDelete } from './hooks/useAdminDelete'
import { useRoleList } from './hooks/useRoleList'
import { getRoleList as getRoleListApi } from '../roles/api/roleService'
import type { SearchParams } from '../types/index'
import type { AdminUser } from '../types/admin'
import { updateAdminStatus, deleteAdmin, batchDeleteAdmins, changePassword } from './service/mockService'
import type { RoleInfo } from '../admin/types/role'


// 使用hooks
const { 
  loading, 
  adminList, 
  pagination, 
  fetchAdminList, 
  handleSizeChange, 
  handleCurrentChange,
  handleSortChange 
} = useAdminList()

const { 
  dialogVisible, 
  dialogTitle, 
  formData, 
  submitLoading, 
  openAddDialog, 
  openEditDialog, 
  handleSubmit 
} = useAdminDialog()

const { handleDelete, handleBatchDelete } = useAdminDelete()
const { roleList, getRoleList } = useRoleList()

// 搜索条件
const searchParams = ref<SearchParams>({})

// 搜索处理
const handleSearch = (params: SearchParams) => {
  searchParams.value = params
  pagination.current_page = 1
  fetchAdminList(params)
}

// 重置搜索
const handleReset = () => {
  searchParams.value = {}
  pagination.current_page = 1
  fetchAdminList()
}

// 刷新数据
const refreshTable = () => {
  fetchAdminList(searchParams.value)
}

// 提交成功后刷新列表
const handleDialogSubmit = async () => {
  const success = await handleSubmit()
  if (success) {
    fetchAdminList()
    ElMessage.success('操作成功')
  }
}

// 删除处理
const handleDeleteConfirm = async (row: AdminUser) => {
  const success = await handleDelete(row.id)
  if (success) {
    refreshTable()
    ElMessage.success('删除成功')
  }
}

// 批量删除处理
const handleBatchDeleteConfirm = async (ids: number[]) => {
  const success = await handleBatchDelete(ids)
  if (success) {
    refreshTable()
    ElMessage.success('批量删除成功')
  }
}

// 导出数据
const handleExport = async () => {
  try {
    // 这里添加导出逻辑
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 处理状态更新
const handleStatusChange = async (id: number, status: number) => {
  try {
    const response = await updateAdminStatus(id, status)
    if (response.code === 0) {
      ElMessage.success('状态更新成功')
      await fetchAdminList()
    }
  } catch (error) {
    ElMessage.error('状态更新失败')
    console.error(error)
  }
}

// 角色列表
const fetchRoleList = async () => {
  try {
    const { data } = await getRoleListApi({ pageSize: 999 })
    roleList.value = data.list
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  }
}

const passwordDialogVisible = ref(false)
const currentUserId = ref<number>(0)

// 处理打开修改密码对话框
const handleChangePassword = (row: AdminUser) => {
  currentUserId.value = row.id!
  passwordDialogVisible.value = true
}

// 处理密码修改提交
const handlePasswordSubmit = async (data: { userId: number, oldPassword: string, newPassword: string }) => {
  try {
    const response = await changePassword(data)
    if (response.code === 0) {
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
    }
  } catch (error) {
    ElMessage.error('密码修改失败')
  }
}

// 查看详情相关
const detailDialogVisible = ref(false)
const currentDetail = ref<AdminUser | null>(null)

const handleView = (row: AdminUser) => {
  currentDetail.value = row
  detailDialogVisible.value = true
}

onMounted(async () => {
  await Promise.all([fetchAdminList(), fetchRoleList()])
})
</script>

<template>
  <div class="admin-container">
    <!-- 标题区域 -->
    <div class="page-header">
      <h2 class="page-title">管理员管理</h2>
      <el-button type="primary" @click="openAddDialog">
        <el-icon><Plus /></el-icon>添加管理员
      </el-button>
    </div>

    <!-- 搜索表单 -->
    <SearchForm
      :loading="loading"
      :role-list="roleList"
      @search="handleSearch"
      @reset="handleReset"
    />

    <!-- 数据表格 -->
    <AdminTable
      :loading="loading"
      :data-list="adminList"
      :pagination="pagination"
      :role-list="roleList"
      @update:pagination="val => pagination = val"
      @refresh="refreshTable"
      @edit="openEditDialog"
      @delete="handleDeleteConfirm"
      @batch-delete="handleBatchDeleteConfirm"
      @sort-change="handleSortChange"
      @export="handleExport"
      @status-change="handleStatusChange"
    />

    <!-- 添加/编辑弹窗 -->
    <AdminDialog
      v-model:visible="dialogVisible"
      :title="dialogTitle"
      :form-data="formData"
      :role-list="roleList"
      :loading="submitLoading"
      @submit="handleDialogSubmit"
    />

    <!-- 添加修改密码对话框 -->
    <ChangePasswordDialog
      v-model="passwordDialogVisible"
      :user-id="currentUserId"
      @submit="handlePasswordSubmit"
    />

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="管理员详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ currentDetail?.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ currentDetail?.real_name }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentDetail?.email }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentDetail?.phone }}</el-descriptions-item>
        <el-descriptions-item label="角色">
          {{ roleList.find(role => role.id === currentDetail?.role_id)?.name || '未知角色' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentDetail?.status === 1 ? 'success' : 'danger'" effect="plain">
            {{ currentDetail?.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentDetail?.create_time }}</el-descriptions-item>
        <el-descriptions-item label="最后登录时间">{{ currentDetail?.last_login_time }}</el-descriptions-item>
        <el-descriptions-item label="最后登录IP">{{ currentDetail?.last_login_ip }}</el-descriptions-item>
        <el-descriptions-item label="登录次数">{{ currentDetail?.login_count }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<style scoped>
.admin-container {
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