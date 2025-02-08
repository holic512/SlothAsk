<script setup lang="ts">
import {ref, computed, onMounted} from 'vue'
import {Search, Plus, Delete} from '@element-plus/icons-vue'
import {handleSearch} from '../service/handleSearch'
import {handleBatchDelete} from '../service/handleBatchDelete'
import {handleBatchStatus} from '../service/handleBatchStatus'
import {useCategoryStore} from '../pinia/categoryStore'
import type {ISearchParams} from '../types/types'
import {apiGetProjectOptions} from '../service/ApiGetProjectOptions'
import {ElMessage} from 'element-plus'

const categoryStore = useCategoryStore()
const keyword = ref<string>('')
const projectId = ref<number>()
const status = ref<number>()

// 状态选项
const statusOptions = [
  {label: '正常', value: 1},
  {label: '禁用', value: 0}
]

// 计算选中行数
const selectedCount = computed((): number => {
  return categoryStore.selectedRows.length
})

// 搜索
const onSearch = async (): Promise<void> => {
  await handleSearch({
    keyword: keyword.value,
    projectId: projectId.value,
    status: status.value,
    pageNum: categoryStore.pageNum,
    pageSize: categoryStore.pageSize
  })
}

// 重置
const onReset = () => {
  keyword.value = ''
  projectId.value = undefined
  status.value = undefined
  onSearch()
}

// 监听项目变化
const onProjectChange = async () => {
  await onSearch()
}

// 监听状态变化
const onStatusChange = async () => {
  await onSearch()
}

// 加载项目选项
const loadProjectOptions = async () => {
  try {
    const response = await apiGetProjectOptions()
    if (response.status === 200) {
      categoryStore.setProjectOptions(response.data)
    } else {
      ElMessage.error(response.message || '获取项目选项失败')
    }
  } catch (error) {
    console.error('加载项目选项失败:', error)
  }
}

onMounted(() => {
  loadProjectOptions()
})

// 处理批量操作
const handleBatchCommand = async (command: string) => {
  try {
    if (categoryStore.selectedRows.length === 0) {
      ElMessage.warning('请先选择要操作的分类')
      return
    }

    const ids = categoryStore.selectedRows.map(row => row.id)

    switch (command) {
      case 'enable':
        await handleBatchStatus(ids, 1)
        break
      case 'disable':
        await handleBatchStatus(ids, 0)
        break
      case 'delete':
        await handleBatchDelete(ids)
        break
    }
  } catch (error) {
    console.error('批量操作失败:', error)
  }
}

// 修改删除选中按钮的点击处理函数
const handleDeleteSelected = async () => {
  if (categoryStore.selectedRows.length === 0) {
    ElMessage.warning('请先选择要删除的分类')
    return
  }
  const ids = categoryStore.selectedRows.map(row => row.id)
  await handleBatchDelete(ids)
}
</script>

<template>
  <div class="search-area">
    <div class="search-group">
      <el-input
          v-model="keyword"
          placeholder="请输入分类名称"
          clearable
          style="width: 240px"
          @keyup.enter="onSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="onSearch">
            搜索
          </el-button>
        </template>
      </el-input>

      <el-select
          v-model="projectId"
          placeholder="选择项目"
          clearable
          style="width: 120px"
          @change="onProjectChange"
      >
        <el-option
            v-for="option in categoryStore.projectOptions"
            :key="option.id"
            :label="option.name"
            :value="option.id"
        >
          <span>{{ option.name }}</span>
          <small style="color: #999"> - {{ option.description }}</small>
        </el-option>
      </el-select>

      <el-select
          v-model="status"
          placeholder="状态"
          clearable
          style="width: 120px"
          @change="onStatusChange"
      >
        <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>

      <el-button @click="onReset">重置</el-button>
    </div>

    <div class="operation-buttons">
      <el-dropdown v-if="selectedCount > 0" @command="handleBatchCommand">
        <el-button type="primary" plain>
          批量操作
          <el-icon class="el-icon--right">
            <arrow-down/>
          </el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="enable">设为正常</el-dropdown-item>
            <el-dropdown-item command="disable">设为禁用</el-dropdown-item>
            <el-dropdown-item command="delete" divided>批量删除</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button
          type="danger"
          :icon="Delete"
          :disabled="selectedCount === 0"
          @click="handleDeleteSelected"
      >
        删除选中({{ selectedCount }})
      </el-button>
      <el-button
          type="primary"
          :icon="Plus"
          @click="categoryStore.openAddForm"
      >
        新增分类
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.search-area {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.search-group {
  flex: 1;
  display: flex;
  gap: 8px;
  align-items: center;
}

.operation-buttons {
  display: flex;
  gap: 8px;
}

@media screen and (max-width: 768px) {
  .search-area {
    flex-direction: column;
  }

  .search-group {
    width: 100%;
  }

  .operation-buttons {
    width: 100%;
    justify-content: flex-end;
  }
}
</style> 