<script setup lang="ts">
import {computed, ref} from 'vue'
import {Delete, Edit, View} from '@element-plus/icons-vue'
import {useCategoryStore} from "../pinia/categoryStore"
import {handleDelete} from "../service/handleDelete"
import {handleSelectionChange} from "../service/handleSelection"
import {ElMessage} from 'element-plus'
import {handleSearch} from "@/view/MainView/user/list/service/handleSearch";

const categoryStore = useCategoryStore()

// 表格高度
const tableHeight = ref('calc(100vh - 240px)')

// 状态格式化
const formatStatus = (status: number) => {
  const statusMap = {
    1: {text: '正常', type: 'success', effect: 'plain'},
    0: {text: '禁用', type: 'danger', effect: 'plain'}
  }
  return statusMap[status] || {text: '未知', type: 'info', effect: 'plain'}
}

// 通过 项目id 获取项目名称
const getNameByProjectId = (id: number) => {
  const item = categoryStore.projectOptions.find(item => item.id === id);
  return item ? item.name : '未找到';
};


</script>

<template>
  <el-table
      :data="categoryStore.tableData"
      style="width: 100%"
      border
      stripe
      :height="tableHeight"
      row-key="id"
      @selection-change="rows => handleSelectionChange(rows)"
  >
    <el-table-column type="selection" width="50" fixed/>
    <el-table-column prop="id" label="ID" width="80" fixed/>
    <el-table-column prop="name" label="分类名称" min-width="120">
      <template #default="{ row }">
        <div style="display: flex; align-items: center; gap: 8px">
          <el-popover
              trigger="click"
              :width="200"
              placement="right"
          >
            <template #reference>
              <el-button link type="primary" class="avatar-trigger">
                <el-avatar
                    size="small"
                    shape="square"
                    :src="row.avatarUrl"
                >
                  {{ row.name.charAt(0) }}
                </el-avatar>
              </el-button>
            </template>

            <!--  头像  -->
            <div class="avatar-preview">
              <el-image
                  :src="row.avatarUrl"
                  fit="contain"
                  preview-teleported
                  style="width: 180px; height: 180px"
              >
                <template #error>
                  <div class="image-error">
                    <el-avatar :size="180" shape="square">
                      {{ row.name.charAt(0) }}
                    </el-avatar>
                  </div>
                </template>
              </el-image>
            </div>
          </el-popover>
          {{ row.name }}
        </div>
      </template>
    </el-table-column>

    <el-table-column prop="projectId" label="所属项目"  show-overflow-tooltip>
      <template #default="{ row }">
        {{ getNameByProjectId(row.projectId) }}
      </template>
    </el-table-column>

    <el-table-column prop="creatorId" label="所属用户" show-overflow-tooltip/>

    <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip/>
    <el-table-column prop="sortOrder" label="权重" width="80" align="center"/>
    <el-table-column prop="viewCount" label="访问量" width="100" align="center"/>
    <el-table-column label="状态" width="90" align="center">
      <template #default="{ row }">
        <el-tag
            :type="formatStatus(row.status).type"
            :effect="formatStatus(row.status).effect"
            size="small"
        >
          {{ formatStatus(row.status).text }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="创建时间" width="180"/>
    <el-table-column label="操作" width="180" fixed="right">
      <template #default="{ row }">
        <div class="operation-buttons">
          <el-tooltip content="查看详情" placement="top">
            <el-button
                link
                type="primary"
                :icon="View"
                @click="categoryStore.openDetailDrawer(row)"
            />
          </el-tooltip>
          <el-tooltip content="编辑" placement="top">
            <el-button
                link
                type="primary"
                :icon="Edit"
                @click="categoryStore.openEditForm(row)"
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

:deep(.el-table__row.sortable-ghost) {
  background-color: var(--el-color-primary-light-9) !important;
}

:deep(.el-table__row.sortable-drag) {
  background-color: var(--el-color-primary-light-8) !important;
  opacity: 0.8;
}

.avatar-trigger {
  height: auto;
  padding: 0;
}

.avatar-preview {
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
</style> 