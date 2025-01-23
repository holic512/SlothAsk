<script setup lang="ts">
import {ref} from 'vue'
import {View, Edit, Delete, Key} from '@element-plus/icons-vue'
import {useUserListStore} from "../pinia/userListStore";
import {handleView} from "../service/handleView";
import {handleSelectionChange} from "../service/handleSelection";
import {handleEdit} from "../service/handleAddEdit";
import {handlePassword} from "../service/handlePassword";
import {handleDelete} from "../service/handleDelete";

// 定义变量属性类
const userListStore = useUserListStore()

// 表格高度
const tableHeight = ref('calc(100vh - 240px)')

// 状态格式化
const formatStatus = (status) => {
  const statusMap = {
    1: {text: '正常', type: 'success', effect: 'plain'},
    0: {text: '禁用', type: 'danger', effect: 'plain'}
  }
  return statusMap[status] || {text: '未知', type: 'info', effect: 'plain'}
}

</script>

<template>
  <el-table
      :data="userListStore.tableData"
      style="width: 100%"
      border
      stripe
      :height="tableHeight"
      @selection-change="rows => handleSelectionChange(rows)"
  >
    <el-table-column type="selection" width="50" fixed/>
    <el-table-column prop="id" label="ID" width="80" fixed/>
    <el-table-column prop="username" label="用户名" min-width="120"/>
    <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip/>
    <el-table-column prop="phone" label="手机号" min-width="120"/>
    <el-table-column label="状态" width="90" align="center">
      <template #default="{ row }">
        <el-tag
            :type="formatStatus(row.status).type"
            :effect="formatStatus(row.status).effect"
            size="small"
            class="status-tag"
        >
          {{ formatStatus(row.status).text }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="240" fixed="right">
      <template #default="{ row }">
        <div class="operation-buttons">
          <el-tooltip content="查看详情" placement="top">
            <el-button
                link
                type="primary"
                :icon="View"
                @click="handleView(row)"
            />
          </el-tooltip>
          <el-tooltip content="编辑" placement="top">
            <el-button
                link
                type="primary"
                :icon="Edit"
                @click="handleEdit(row)"
            />
          </el-tooltip>
          <el-tooltip content="修改密码" placement="top">
            <el-button
                link
                type="warning"
                :icon="Key"
                @click="handlePassword( row)"
            />
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button
                link
                type="danger"
                :icon="Delete"
                @click="handleDelete(row)"
            />
          </el-tooltip>
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
.operation-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
}

:deep(.status-tag) {
  min-width: 60px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  height: 24px;
  padding: 0 8px;
  font-size: 12px;
  border-radius: 4px;
  font-weight: 500;
}

:deep(.status-tag.el-tag--success) {
  --el-tag-bg-color: var(--el-color-success-light-9);
  --el-tag-border-color: var(--el-color-success-light-5);
  --el-tag-text-color: var(--el-color-success);
}

:deep(.status-tag.el-tag--danger) {
  --el-tag-bg-color: var(--el-color-danger-light-9);
  --el-tag-border-color: var(--el-color-danger-light-5);
  --el-tag-text-color: var(--el-color-danger);
}

:deep(.status-tag.el-tag--info) {
  --el-tag-bg-color: var(--el-color-info-light-9);
  --el-tag-border-color: var(--el-color-info-light-5);
  --el-tag-text-color: var(--el-color-info);
}

:deep(.el-table__cell) {
  padding: 6px 0;
}

:deep(.el-table .cell) {
  padding: 0 8px;
}

:deep(.el-button.is-link) {
  padding: 4px;
  border-radius: 4px;
}

:deep(.el-button.is-link:hover) {
  background-color: var(--el-fill-color-light);
}
</style> 