<script setup lang="ts">
import {View, Edit, Delete} from '@element-plus/icons-vue'
import {useQuestionStore} from '../pinia/questionStore'
import type {IQuestion} from '../types/types'
import {ElMessageBox, ElMessage} from 'element-plus'
import {ref} from "vue";
import {ApiPatchUpdateStatus} from "@/view/MainView/question/list/service/ApiPatchUpdateStatus";
import { debounce } from "lodash";
import {apiDeleteQuestion} from "@/view/MainView/question/list/service/ApiDeleteQuestion";
import {refreshData} from "@/view/MainView/question/list/service/refreshData";
const questionStore = useQuestionStore()

// 表格高度
const tableHeight = ref('calc(100vh - 240px)')

// 格式化日期时间
const formatDateTime = (datetime: string) => {
  return new Date(datetime).toLocaleString()
}

// 难度标签配置
const difficultyConfig = {
  1: {label: '简单', type: 'success'},
  2: {label: '中等', type: 'warning'},
  3: {label: '困难', type: 'danger'}
}

// 题目类型配置
const typeConfig = {
  1: {label: '单选', type: 'primary'},
  2: {label: '多选', type: 'success'},
  3: {label: '判断', type: 'warning'},
  4: {label: '简答', type: 'info'}
}

// 获取难度标签
const getDifficultyLabel = (difficulty: number) => {
  return difficultyConfig[difficulty]?.label || '未知'
}

const getDifficultyTag = (difficulty: number) => {
  return difficultyConfig[difficulty]?.type || ''
}

// 获取类型标签
const getTypeLabel = (type: number) => {
  return typeConfig[type]?.label || '未知'
}

const getTypeTag = (type: number) => {
  return typeConfig[type]?.type || ''
}

// 获取项目名称（需要从 store 中获取 项目 列表）
const getProjectName = (projectId: number) => {
  const project = questionStore.projectOptions.find(item => item.id === projectId);
  return project?.name
}

// 获取分类名称
const getCategoryName = (categoryId: number) => {
  const category = questionStore.categoryOptions.find(item => item.id === categoryId);
  return category?.name
}

// 获取标签名称（
const getTagName = (tagIds: number[]) => {
  // 遍历数组，找到对应的名称，再拼接成字符串返回
  const tagNames = tagIds.map(tagId => {
    const tag = questionStore.tagOptions.find(item => item.id === tagId);
    return tag?.name;
  }).filter(name => name); // 过滤掉 undefined
  return tagNames; // 返回标签名称数组
}

// 处理选择变化
const handleSelectionChange = (selection: IQuestion[]) => {
  questionStore.setSelectedRows(selection)
}

// 处理查看详情
const handleViewDetail = (row: IQuestion) => {
  questionStore.setCurrentQuestion(row)
  questionStore.setDetailVisible(true)
}

// 处理编辑
const handleEdit = (row: IQuestion) => {
  questionStore.formType = 'edit'
  questionStore.setCurrentQuestion(row)
  questionStore.setFormVisible(true)
}

// 处理删除
const handleDelete = async (row: IQuestion) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除题目 "${row.title}" 吗？`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )
    await apiDeleteQuestion(row.id)
    ElMessage.success('删除成功')


    // 计算新 total
    let newTotal = questionStore.total - 1;
    newTotal = Math.max(0, newTotal); // 避免负数

    // 计算新 pageNum
    let newPageNum = questionStore.pageNum;
    if (newTotal > 0 && newTotal % questionStore.pageSize === 0) {
      // 当前页数据被清空，回退到上一页
      newPageNum = Math.max(1, questionStore.pageNum - 1);
    }

    // 更新 store 页码
    questionStore.pageNum = newPageNum;
    questionStore.total = newTotal;

    await refreshData();

  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}




const handleStatusChange = debounce(async (row: IQuestion) => {
  const result = await ApiPatchUpdateStatus(row.id);

  if (result.status === 200) {
    ElMessage.success("状态更新成功");
  } else {
    ElMessage.error("状态更新失败");
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1;
  }
}, 500); // 500ms 内连续点击只会触发一次


// 处理分页大小变化
const handleSizeChange = (size: number) => {
  questionStore.pageSize = size
  emit('refresh')
}

// 处理页码变化
const handleCurrentChange = (page: number) => {
  questionStore.pageNum = page
  emit('refresh')
}

const emit = defineEmits(['refresh'])
</script>
<template>
  <div class="question-table">
    <el-table
        v-loading="questionStore.loading"
        :data="questionStore.tableData"
        style="width: 100%"
        border
        stripe
        :height="tableHeight"
        @selection-change="handleSelectionChange"
    >
      <!-- 选择列 -->
      <el-table-column type="selection" width="55" fixed/>

      <!-- ID列 -->
      <el-table-column prop="id" label="ID" width="80" fixed/>

      <!-- 标题列 -->
      <el-table-column prop="title" label="题目标题" min-width="160" show-overflow-tooltip>
        <template #default="{ row }">
          <el-button link type="primary" @click="handleViewDetail(row)">
            {{ row.title }}
          </el-button>
        </template>
      </el-table-column>

      <!-- 项目列 -->
      <el-table-column prop="projectId" label="所属项目" width="120">
        <template #default="{ row }">
          {{ getProjectName(row.projectId) }}
        </template>
      </el-table-column>

      <!-- 分类列 -->
      <el-table-column prop="categoryId" label="所属分类" width="120">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>

      <!-- 难度列 -->
      <el-table-column prop="difficulty" label="难度" width="100">
        <template #default="{ row }">
          <el-tag :type="getDifficultyTag(row.difficulty)" size="small">
            {{ getDifficultyLabel(row.difficulty) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 题目类型列 -->
      <el-table-column prop="type" label="题目类型" width="100">
        <template #default="{ row }">
          <el-tag :type="getTypeTag(row.type)" size="small">
            {{ getTypeLabel(row.type) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 标签列 -->
      <el-table-column prop="tagCategoryIds" label="标签" min-width="60">
        <template #default="{ row }">
          <div>
            <el-tag
                v-for="(tag, index) in getTagName(row.tagCategoryIds)"
                :key="index"
                size="small"
                type="info"
            >
              {{ tag }}
            </el-tag>
          </div>
        </template>
      </el-table-column>

      <!-- 浏览次数列 -->
      <el-table-column prop="viewCount" label="浏览次数" width="100" align="center"/>

      <!-- 状态列 -->
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
          />
        </template>
      </el-table-column>

      <!-- 创建时间列 -->
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDateTime(row.createTime) }}
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <div class="operation-buttons">
            <el-tooltip content="查看详情" placement="top">
              <el-button
                  link
                  type="primary"
                  :icon="View"
                  @click="handleViewDetail(row)"
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

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"

          v-model:current-page="questionStore.pageNum"
          v-model:page-size="questionStore.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="questionStore.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>


<style scoped>
.question-table {
  display: flex;
  flex-direction: column;

}

.operation-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table .cell) {
  padding: 8px;
}

:deep(.el-button.is-link) {
  padding: 4px;
  border-radius: 4px;
}

:deep(.el-button.is-link:hover) {
  background-color: var(--el-fill-color-light);
}

:deep(.el-tag) {
  border-radius: 4px;
}
</style> 