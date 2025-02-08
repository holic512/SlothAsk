<!--./admin/components/AdminTable.vue -->
<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete, Refresh, Setting, View, Download, Lock, Key } from '@element-plus/icons-vue'
import type { AdminUser, PaginationInfo } from '../types/index'
import type { TableColumnCtx } from 'element-plus'
import type { RoleInfo } from '../../roles/types/role'

// 使用常量定义角色类型映射
const ROLE_TYPE_MAP = {
  admin: 'danger',
  editor: 'warning',
  user: 'success'
} as const

type RoleType = keyof typeof ROLE_TYPE_MAP
type TagType = 'primary' | 'success' | 'info' | 'warning' | 'danger'

interface PaginationProps {
  current_page: number
  page_size: number
  total: number
}

// 更清晰的 props 定义
const props = defineProps<{
  loading: boolean
  dataList: AdminUser[]
  pagination: PaginationInfo
  roleList: RoleInfo[]
}>()

// Emits 定义
const emit = defineEmits<{
  'update:pagination': [pagination: PaginationInfo]
  'refresh': []
  'delete': [id: number]
  'batch-delete': [ids: number[]]
  'batch-disable': [ids: number[]]
  'edit': [row: AdminUser]
  'sort-change': [{ prop: string, order: 'ascending' | 'descending' | null }]
  'selection-change': [rows: AdminUser[]]
  'view': [row: AdminUser]
  'export': []
  'change-password': [row: AdminUser]
  'size-change': [size: number]
  'current-change': [page: number]
}>()

// 表格列定义
const allColumns = [
  { prop: 'username', label: '用户名', minWidth: 120 },
  { prop: 'real_name', label: '真实姓名', minWidth: 120 },
  { prop: 'email', label: '邮箱', minWidth: 180 },
  { prop: 'phone', label: '手机号', minWidth: 120 },
  { prop: 'role_name', label: '角色', minWidth: 100 },
  { prop: 'status', label: '状态', minWidth: 80 },
  { prop: 'create_time', label: '创建时间', minWidth: 180, sortable: true },
  { prop: 'last_login_time', label: '最后登录时间', minWidth: 180, sortable: true },
]

// 列显示控制
const showColumnSelector = ref(false)
const selectedColumns = ref(allColumns.map(col => col.prop))
const visibleColumns = computed(() => 
  allColumns.filter(col => selectedColumns.value.includes(col.prop))
)

// 选择行相关
const selectedRows = ref<AdminUser[]>([])
const handleSelectionChange = (rows: AdminUser[]) => {
  selectedRows.value = rows
  emit('selection-change', rows)
}

// 批量删除
const handleBatchDelete = () => {
  if (!selectedRows.value.length) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 条记录吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    emit('batch-delete', selectedRows.value.map(row => row.id!))
  }).catch(() => {})
}

// 批量禁用
const handleBatchDisable = () => {
  if (!selectedRows.value.length) {
    ElMessage.warning('请选择要禁用的记录')
    return
  }
  
  ElMessageBox.confirm(
    `确定要禁用选中的 ${selectedRows.value.length} 条记录吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    emit('batch-disable', selectedRows.value.map(row => row.id!))
  }).catch(() => {})
}

// 导出数据
const handleExport = () => {
  emit('export')
}

// 刷新表格
const handleRefresh = () => {
  emit('refresh')
}

// 编辑和删除操作
const handleEdit = (row: AdminUser) => {
  emit('edit', row)
}

const handleDelete = async (row: AdminUser) => {
  try {
    await ElMessageBox.confirm('确定要删除该记录吗？', '提示', {
      type: 'warning'
    })
    emit('delete', row.id!)
  } catch {
    // 用户取消删除
  }
}

// 排序变化
const handleSortChange = ({ prop, order }: { prop: string, order: 'ascending' | 'descending' | null }) => {
  emit('sort-change', { prop, order })
}

// 使用 computed 优化 roleMap 查找性能
const roleMap = computed(() => {
  if (!props.roleList) return new Map()
  return new Map(props.roleList.map(role => [role.id, role]))
})

// 优化 getRoleType 方法
const getRoleType = (roleId: number): TagType => {
  const role = roleMap.value.get(roleId)
  const roleType = role?.type as RoleType
  return ROLE_TYPE_MAP[roleType] || 'info'
}

// 分页处理方法
const handleSizeChange = (size: number) => {
  const newPagination = {
    ...props.pagination,
    page_size: size,
    current_page: 1 // 重置到第一页
  }
  emit('update:pagination', newPagination)
  emit('size-change', size)
}

const handleCurrentChange = (page: number) => {
  const newPagination = {
    ...props.pagination,
    current_page: page
  }
  emit('update:pagination', newPagination)
  emit('current-change', page)
}

const handleView = (row: AdminUser) => {
  emit('view', row)
}

// 添加修改密码处理函数
const handleChangePassword = (row: AdminUser) => {
  emit('change-password', row)
}
</script>

<template>
  <div class="admin-table">
    <!-- 工具栏 -->
    <div class="table-toolbar">
      <div class="left">
        <el-button-group>
          <el-button
            type="danger"
            :disabled="!selectedRows.length"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button
            type="warning"
            :disabled="!selectedRows.length"
            @click="handleBatchDisable"
          >
            <el-icon><Lock /></el-icon>
            批量禁用
          </el-button>
        </el-button-group>
      </div>
      <div class="right">
        <el-button-group>
          <el-button @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
          <el-button type="primary" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="dataList"
      border
      stripe
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="80" align="center" />
      <el-table-column prop="username" label="用户名" min-width="120" sortable align="center" />
      <el-table-column prop="real_name" label="真实姓名" min-width="120" sortable align="center" />
      <el-table-column prop="email" label="邮箱" min-width="180" align="center" />
      <el-table-column prop="phone" label="手机号" min-width="120" align="center" />
      <el-table-column prop="role_name" label="角色" min-width="120" align="center">
        <template #default="{ row }">
          {{ roleList.find(role => role.id === row.role_id)?.name || '未知角色' }}
        </template>
      </el-table-column>
      <el-table-column prop="create_time" label="创建时间" min-width="180" sortable align="center" />
      <el-table-column prop="last_login_time" label="最后登录时间" min-width="180" sortable align="center" />
      <el-table-column prop="update_time" label="更新时间" min-width="180" sortable align="center" />
      <el-table-column prop="last_login_ip" label="最后登录IP" min-width="180" sortable align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag
            :type="row.status === 1 ? 'success' : 'danger'"
            effect="plain"
            size="small"
          >
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" fixed="right" align="center">
        <template #default="{ row }">
          <el-button-group>
            <el-tooltip content="查看详情" placement="top">
              <el-button
                type="primary"
                :icon="View"
                circle
                plain
                @click="handleView(row)"
              />
            </el-tooltip>
            <el-tooltip content="编辑" placement="top">
              <el-button
                type="primary"
                :icon="Edit"
                circle
                plain
                @click="handleEdit(row)"
              />
            </el-tooltip>
            <el-tooltip content="修改密码" placement="top">
              <el-button
                type="warning"
                :icon="Key"
                circle
                plain
                @click="handleChangePassword(row)"
              />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button
                type="danger"
                :icon="Delete"
                circle
                plain
                @click="handleDelete(row)"
              />
            </el-tooltip>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current_page"
        v-model:page-size="pagination.page_size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 列显示控制弹窗 -->
    <el-dialog
      v-model="showColumnSelector"
      title="列显示设置"
      width="300px"
    >
      <el-checkbox-group v-model="selectedColumns">
        <el-checkbox
          v-for="col in allColumns"
          :key="col.prop"
          :label="col.prop"
        >
          {{ col.label }}
        </el-checkbox>
      </el-checkbox-group>
    </el-dialog>
  </div>
</template>

<style scoped>
.admin-table {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-button-group) {
  .el-button {
    padding: 5px;
    margin: 0;
  }
}

:deep(.el-table) {
  --el-table-border-color: #ebeef5;
  --el-table-header-bg-color: #f5f7fa;
}

:deep(.el-table th) {
  font-weight: 600;
  color: #1f2f3d;
}
</style> 