<script setup lang="ts">
import {ref, computed} from 'vue'
import {Search, Plus, Delete} from '@element-plus/icons-vue'
import {statusOptions} from '../constants/statusOptions'
import {handleSearch} from '../service/handleSearch'
import {handleAdd} from '../service/handleAddEdit'
import {handleBatchDelete} from '../service/handleBatchDelete'
import {useUserListStore} from '../pinia/userListStore'
import {ISearchParams} from "@/view/MainView/user/list/types/search";

// 获取 store 实例
const userListStore = useUserListStore()

// 搜索表单数据
const keyword = ref<string>('')
const status = ref<number>()

// 计算选中行数
const selectedCount = computed((): number => {
  return userListStore.selectedRows.length
})

/**
 * 处理搜索提交
 */
const onSearch = async (): Promise<void> => {
  await handleSearch(<ISearchParams>{
    keyword: keyword.value,
    status: status.value
  })
}

/**
 * 处理状态变化
 */
const onStatusChange = async (): Promise<void> => {
  await onSearch()
}
</script>

<template>
  <div class="search-area">
    <div class="search-group">
      <el-input
          v-model="keyword"
          placeholder="请输入用户名/邮箱/手机号"
          clearable
          style="width: 300px"
      >
        <template #append>
          <el-button :icon="Search" @click="onSearch">
            搜索
          </el-button>
        </template>
      </el-input>
      <el-select
          v-model="status"
          placeholder="用户状态"
          class="status-select"
          @change="onStatusChange"
      >
        <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
    </div>
    <div class="operation-buttons">
      <el-button
          type="danger"
          :icon="Delete"
          :disabled="selectedCount === 0"
          @click="handleBatchDelete"
      >
        删除选中({{ selectedCount }})
      </el-button>
      <el-button
          type="primary"
          :icon="Plus"
          @click="handleAdd"
      >
        新增用户
      </el-button>
    </div>
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

.search-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.status-select {
  width: 120px;
}

.operation-buttons {
  display: flex;
  gap: 8px;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .search-group {
    flex-direction: column;
  }

  .status-select {
    width: 100%;
  }

  .operation-buttons {
    justify-content: flex-end;
  }
}

@media screen and (max-width: 480px) {
  .operation-buttons {
    flex-direction: column;
    width: 100%;
  }

  .operation-buttons .el-button {
    width: 100%;
  }
}
</style> 