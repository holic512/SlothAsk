<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { storeToRefs } from 'pinia'
import ProjectTable from './components/ProjectTable.vue'
import SearchForm from "./components/SearchForm.vue"
import ProjectForm from './components/ProjectForm.vue'
import ProjectDetail from './components/ProjectDetail.vue'
import { useProjectStore } from './pinia/project'

const projectStore = useProjectStore()
const { 
  projects, 
  loading, 
  total, 
  currentPage, 
  pageSize, 
  searchKeyword 
} = storeToRefs(projectStore)

// 选中行
const selectedRows = ref([])

// 表单相关
const formVisible = ref(false)
const formType = ref('add')
const currentEditProject = ref(null)

// 项目详情相关
const showDetail = ref(false)
const detailData = ref({})

// 在组件挂载时获取项目列表
onMounted(() => {
  projectStore.fetchProjects()
})

// 处理搜索
const handleSearch = (keyword) => {
  projectStore.setSearchKeyword(keyword)
  projectStore.fetchProjects()
}

// 处理排序
const handleSort = (order) => {
  projectStore.setSortType(order === 'asc' ? 1 : 2)
  projectStore.fetchProjects()
}

// 处理批量删除
const handleBatchDelete = () => {
  if (!selectedRows.value.length) return
  
  ElMessageBox.confirm(
    `确认删除选中的 ${selectedRows.value.length} 个项目吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    const ids = selectedRows.value.map(row => row.id)
    await projectStore.batchDeleteProjects(ids)
    selectedRows.value = []
  }).catch(() => {})
}

// 处理新增
const handleAdd = () => {
  formType.value = 'add'
  currentEditProject.value = null
  formVisible.value = true
}

// 处理选择变化
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 查看详情处理函数
const handleView = (row) => {
  detailData.value = row
  showDetail.value = true
}

// 处理编辑
const handleEdit = (row) => {
  formType.value = 'edit'
  currentEditProject.value = { ...row }
  formVisible.value = true
}

// 处理表单提交
const handleFormSubmit = async (formData) => {
  try {
    if (formType.value === 'add') {
      await projectStore.createProject(formData)
    } else {
      await projectStore.updateProject(formData)
    }
    formVisible.value = false
  } catch (error) {
    console.error('操作失败:', error)
  }
}

// 处理删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确认删除该项目吗？',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    await projectStore.deleteProject(row.id)
  }).catch(() => {})
}
</script>

<template>
  <div class="project-list-container">
    <!-- 搜索和操作区域 -->
    <SearchForm
      v-model:keyword="searchKeyword"
      v-model:selectedCount="selectedRows.length"
      @search="handleSearch"
      @sort="handleSort"
      @add="handleAdd"
      @batch-delete="handleBatchDelete"
    />

    <!-- 项目表格 -->
    <ProjectTable
      v-model:data="projects"
      v-model:loading="loading"
      @selection-change="rows => selectedRows = rows"
      @view="row => { showDetail = true; detailData = row }"
      @edit="handleEdit"
      @delete="handleDelete"
    />

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>
  </div>

  <!-- 项目表单 -->
  <ProjectForm
    v-model:visible="formVisible"
    :type="formType"
    :data="currentEditProject"
    @submit="handleFormSubmit"
  />

  <!-- 项目详情弹窗 -->
  <ProjectDetail
    v-model:visible="showDetail"
    :data="detailData"
  />
</template>

<style scoped>
.project-list-container {
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