<script setup lang="ts">
import { onMounted } from 'vue'
import SearchForm from './components/SearchForm.vue'
import CategoryTable from './components/CategoryTable.vue'
import CategoryForm from './components/CategoryForm.vue'
import CategoryDetail from './components/CategoryDetail.vue'
import { useCategoryStore } from "./pinia/categoryStore"
import { apiGetCategoryList } from "./service/ApiGetCategoryList"

// 使用 store
const categoryStore = useCategoryStore()

// 加载分类列表数据
const loadCategoryList = async () => {
  categoryStore.setLoading(true)
  try {
    const categoryListData = await apiGetCategoryList({
      keyword: categoryStore.searchKeyword,
      projectId: categoryStore.searchProjectId,
      status: categoryStore.searchStatus,
      pageNum: categoryStore.pageNum,
      pageSize: categoryStore.pageSize
    })

    if (categoryListData.status === 200) {
      categoryStore.tableData = categoryListData.data.list
      categoryStore.total = categoryListData.data.total
    }
  } catch (error) {
    console.error('加载分类列表失败:', error)
  } finally {
    categoryStore.setLoading(false)
  }
}

// 页面加载时获取数据
onMounted(async () => {
  await loadCategoryList()
})

// 处理分页变化
const handleCurrentChange = async (page: number) => {
  categoryStore.pageNum = page
  await loadCategoryList()
}

const handleSizeChange = async (size: number) => {
  categoryStore.pageSize = size
  categoryStore.pageNum = 1
  await loadCategoryList()
}
</script>

<template>
  <div class="category-container">
    <!-- 搜索表单 -->
    <SearchForm @search="loadCategoryList" />

    <!-- 分类表格 -->
    <CategoryTable />

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="categoryStore.total"
        :page-sizes="[10, 20, 50, 100]"
        :current-page="categoryStore.pageNum"
        :page-size="categoryStore.pageSize"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 分类表单 -->
    <CategoryForm @success="loadCategoryList" />

    <!-- 分类详情抽屉 -->
    <CategoryDetail />
  </div>
</template>

<style scoped>
.category-container {
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