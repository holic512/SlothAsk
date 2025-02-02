<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import ProjectTable from './components/ProjectTable.vue'
import SearchForm from "./components/SearchForm.vue";
import ProjectForm from './components/ProjectForm.vue'
import ProjectDetail from './components/ProjectDetail.vue'
import { useProjectStore } from './pinia/project'
import { storeToRefs } from 'pinia'

const projectStore = useProjectStore()
const { projects, loading } = storeToRefs(projectStore)

// 选中行
const selectedRows = ref([])

// 表单相关
const formVisible = ref(false)
const formType = ref('add')
const currentEditProject = ref(null)

// 项目详情相关
const showDetail = ref(false)
const detailData = ref({})

const searchKeyword = ref('')

// 获取格式化的当前时间
const getCurrentTime = () => {
  const now = new Date()
  return now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-')
}

// 处理搜索
const handleSearch = (keyword) => {
  console.log('搜索关键词：', keyword)
  // TODO: 实现搜索逻辑
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

// 处理批量删除
const handleBatchDelete = () => {
  
  ElMessageBox.confirm(
    `确认删除选中的 ${selectedRows.value.length} 个项目吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // TODO: 调用批量删除 API
    console.log('批量删除：', selectedRows.value.map(row => row.id))
    ElMessage.success('删除成功')
    // 删除成功后清空选中
    selectedRows.value = []
  }).catch(() => {})
}

// 查看详情处理函数
const handleView = (row) => {
  detailData.value = row
  showDetail.value = true
}

// 处理编辑
const handleEdit = (row) => {
  formType.value = 'edit'
  currentEditProject.value = { ...row }  // 使用浅拷贝避免直接修改原数据
  formVisible.value = true
}

// 处理表单提交
const handleFormSubmit = async (formData) => {
  try {
    if (formType.value === 'add') {
      await projectStore.createProject(formData)
      ElMessage.success('新增成功')
    } else {
      await projectStore.updateProject(formData)
      ElMessage.success('编辑成功')
    }
    formVisible.value = false
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 处理排序
const handleSort = (order) => {
  // 对响应式数组进行排序
  projects.value.sort((a, b) => {
    if (order === 'asc') {
      return a.sort_order - b.sort_order
    } else {
      return b.sort_order - a.sort_order
    }
  })
  ElMessage.success(`已按序号${order === 'asc' ? '正序' : '倒序'}排列`)
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
  ).then(() => {
    const index = projects.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      projectStore.deleteProject(row.id)
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

// 在组件挂载时获取项目列表
onMounted(async () => {
  await projectStore.fetchProjects()
})

// 添加一个调试日志
watch(() => projects.value, (newVal) => {
  console.log('Projects data:', newVal)
}, { immediate: true })
</script>

<template>
  <div class="project-list-container">
    <!-- 搜索表单 -->
    <SearchForm
      v-model:selectedCount="selectedRows.length"
      v-model:keyword="searchKeyword"
      @search="handleSearch"
      @add="handleAdd"
      @batch-delete="handleBatchDelete"
      @sort="handleSort"
    />

    <!-- 项目表格 -->
    <ProjectTable
      :data="projects"
      v-model:loading="loading"
      @view="handleView"
      @edit="handleEdit"
      @delete="handleDelete"
      @selection-change="handleSelectionChange"
    />
  
  <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="100"
          :page-sizes="[10, 20, 50, 100]"
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