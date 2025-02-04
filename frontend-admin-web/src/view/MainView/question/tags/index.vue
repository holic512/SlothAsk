<script setup lang="ts">
import {ref, onMounted} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus'
import TagTable from './components/TagTable.vue'
import SearchForm from "./components/SearchForm.vue"
import TagForm from './components/TagForm.vue'
import {useTagStore} from './pinia/tags'
import {storeToRefs} from 'pinia'
import type {TagCategory} from './types/TagCategoryType'
import {ApiGetProjectList} from "@/view/MainView/question/tags/services/ApiGetProjectList";
import {ApiGetTagsList} from "@/view/MainView/question/tags/services/ApiGetTagsList";
import {handleLoadTagsList} from "@/view/MainView/question/tags/services/handleLoadTagsList";
import { ApiDeleteTag } from './services/ApiDeleteTag'
import { ApiDeleteBatchTags } from './services/ApiDeleteBatchTags'

const tagStore = useTagStore()
const {tags} = storeToRefs(tagStore)

// 选中行
const selectedRows = ref<TagCategory[]>([])

// 表单相关
const formVisible = ref(false)
const formType = ref<'add' | 'edit'>('add')
const currentEditTag = ref<TagCategory | null>(null)

// 处理搜索
const handleSearch = async (keyword: string) => {
  tagStore.searchKeyword = keyword
  await handleLoadTagsList()
}

// 处理页码变化
const handlePageChange = async (page: number) => {
  tagStore.currentPage = page
  await handleLoadTagsList()
}

// 处理每页条数变化
const handleSizeChange = async (size: number) => {
  tagStore.pageSize = size
  tagStore.currentPage = 1
  await handleLoadTagsList()
}

// 处理新增
const handleAdd = () => {
  formType.value = 'add'
  currentEditTag.value = null
  formVisible.value = true
}

// 处理选择变化
const handleSelectionChange = (rows: TagCategory[]) => {
  selectedRows.value = rows
}

// 处理批量删除
const handleBatchDelete = () => {
    ElMessageBox.confirm(
        `确认删除选中的 ${selectedRows.value.length} 个标签吗？`,
        '删除确认',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(async () => {
        try {
            const ids = selectedRows.value
                .map(row => row.id)
                .filter((id): id is number => id !== undefined)
            
            const response = await ApiDeleteBatchTags(ids)
            if (response.status === 200) {
                ElMessage.success(response.message || '批量删除成功')
                selectedRows.value = []
                await handleLoadTagsList()  // 重新加载列表
            } else {
                ElMessage.error(response.message || '批量删除失败')
            }
        } catch (error) {
            ElMessage.error('操作失败')
        }
    }).catch(() => {
        // 用户取消删除，不做处理
    })
}

// 处理编辑
const handleEdit = (row: TagCategory) => {
  formType.value = 'edit'
  currentEditTag.value = {...row}
  formVisible.value = true
}

// 处理排序
const handleSort = (order: 'asc' | 'desc') => {
  tags.value.sort((a, b) => {
    return order === 'asc'
        ? a.sortOrder - b.sortOrder
        : b.sortOrder - a.sortOrder
  })
  ElMessage.success(`已按序号${order === 'asc' ? '正序' : '倒序'}排列`)
}

// 处理删除
const handleDelete = (row: TagCategory) => {
    ElMessageBox.confirm(
        `确认删除标签"${row.name}"吗？`,
        '删除确认',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(async () => {
        try {
            if (row.id === undefined) {
                ElMessage.error('标签ID不存在')
                return
            }

            const response = await ApiDeleteTag(row.id)
            if (response.status === 200) {
                ElMessage.success(response.message || '删除成功')
                await handleLoadTagsList()  // 重新加载列表
            } else {
                ElMessage.error(response.message || '删除失败')
            }
        } catch (error) {
            ElMessage.error('操作失败')
        }
    }).catch(() => {
        // 用户取消删除，不做处理
    })
}

onMounted(async () => {
  tagStore.projectList = await ApiGetProjectList()
  await handleLoadTagsList()
})
</script>

<template>
  <div class="tag-list-container">
    <!-- 搜索表单 -->
    <SearchForm
        v-model:selectedCount="selectedRows.length"
        v-model:keyword="tagStore.searchKeyword"
        @search="handleSearch"
        @add="handleAdd"
        @batch-delete="handleBatchDelete"
        @sort="handleSort"
    />

    <!-- 标签表格 -->
    <TagTable
        v-model="tags"
        @edit="handleEdit"
        @delete="handleDelete"
        @selection-change="handleSelectionChange"
    />

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="tagStore.total"
          :current-page="tagStore.currentPage"
          :page-size="tagStore.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
      />
    </div>
  </div>

  <!-- 标签表单 -->
  <TagForm
      v-model:visible="formVisible"
      v-model:type="formType"
      v-model:data="currentEditTag"
  />
</template>

<style scoped>
.tag-list-container {
  padding: 16px;
  background-color: var(--el-bg-color);
  border-radius: 4px;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style> 