<script setup lang="ts">
import { ref } from 'vue'
import { ElMessageBox } from 'element-plus'
import { computed } from 'vue'
import { mockPermissionTemplates, mockRolePermissions } from '../../mock'
import type { RoleInfo } from '../types/role'
import { PermissionLevel } from '../types/permission'

const props = defineProps<{
  loading: boolean
  dataList: RoleInfo[]
  pagination: {
    current_page: number
    page_size: number
    total: number
  }
}>()

const emit = defineEmits<{
  'update:pagination': [value: any]
  'config-permission': [row: RoleInfo]
  'view-permissions': [row: RoleInfo]
  edit: [row: RoleInfo]
  delete: [row: RoleInfo]
  'sort-change': [params: { prop: string, order: string }]
}>()

const handleSizeChange = (size: number) => {
  emit('update:pagination', { ...props.pagination, page_size: size })
}

const handleCurrentChange = (page: number) => {
  emit('update:pagination', { ...props.pagination, current_page: page })
}

const handleConfigPermission = (row: RoleInfo) => {
  emit('config-permission', row)
}

const handleEdit = (row: RoleInfo) => {
  emit('edit', row)
}

const handleDelete = (row: RoleInfo) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    type: 'warning'
  }).then(() => {
    emit('delete', row)
  })
}

const handleViewPermissions = (row: RoleInfo) => {
  emit('view-permissions', row)
}

// 计算角色的权限等级
const getPermissionLevel = (row: RoleInfo): PermissionLevel => {
  const permissions = mockRolePermissions[row.id]?.permission_ids || []
  const permissionCount = permissions.length
  
  if (permissionCount >= 12) return PermissionLevel.SUPER
  if (permissionCount >= 6) return PermissionLevel.HIGH
  if (permissionCount >= 3) return PermissionLevel.MEDIUM
  return PermissionLevel.LOW
}

// 权限等级标签样式
const getLevelTag = (level: PermissionLevel) => {
  const config = {
    [PermissionLevel.SUPER]: { type: 'danger', label: '超级管理员' },
    [PermissionLevel.HIGH]: { type: 'warning', label: '高级权限' },
    [PermissionLevel.MEDIUM]: { type: 'success', label: '中级权限' },
    [PermissionLevel.LOW]: { type: 'info', label: '基础权限' }
  }
  return config[level]
}
</script>

<template>
  <el-card class="table-card">
    <el-table
      v-loading="loading"
      :data="dataList"
      border
      style="width: 100%"
      :header-cell-style="{
        background: '#f5f7fa',
        color: '#606266'
      }"
      @sort-change="$emit('sort-change', $event)"
    >
      <el-table-column prop="id" label="ID" width="80" sortable="custom" />
      <el-table-column prop="name" label="角色名称" sortable="custom" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="create_time" label="创建时间" width="180" sortable="custom" />
      <el-table-column label="权限等级" width="120">
        <template #default="{ row }">
          <el-tag
            :type="getLevelTag(getPermissionLevel(row)).type"
            effect="light"
          >
            {{ getLevelTag(getPermissionLevel(row)).label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="权限数量" width="100">
        <template #default="{ row }">
          <el-tooltip
            :content="`菜单权限: ${mockRolePermissions[row.id]?.menu_ids.length || 0}个<br/>操作权限: ${mockRolePermissions[row.id]?.permission_ids.length || 0}个`"
            raw-content
          >
            <span class="permission-count">
              {{ mockRolePermissions[row.id]?.permission_ids.length || 0 }}
            </span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button
            type="primary"
            link
            @click="handleConfigPermission(row)"
            :disabled="row.id === 1"
          >
            配置权限
          </el-button>
          <el-button
            type="primary"
            link
            @click="handleEdit(row)"
            :disabled="row.id === 1"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            link
            @click="handleDelete(row)"
            :disabled="row.id === 1"
          >
            删除
          </el-button>
          <el-button
            type="primary"
            link
            @click="handleViewPermissions(row)"
          >
            查看权限
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current_page"
        v-model:page-size="pagination.page_size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </el-card>
</template>

<style scoped>
.table-card {
  margin-bottom: 20px;
  :deep(.el-card__body) {
    padding: 0;
  }
}

.pagination-container {
  margin-top: 20px;
  padding: 10px 20px;
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
}

:deep(.el-table) {
  border-radius: 4px;
  overflow: hidden;
}

:deep(.el-button--link) {
  padding: 2px 0;
}

:deep(.el-tag) {
  border-radius: 2px;
}

.permission-count {
  cursor: help;
  color: var(--el-text-color-regular);
}
</style> 