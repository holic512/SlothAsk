<template>
  <el-drawer
      v-model="drawerVisible"
      title="题目详情"
      size="60%"
      :destroy-on-close="true"
  >
    <div class="question-detail">
      <!-- 基本信息卡片 -->
      <el-card class="detail-card">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
            <div class="header-tags">
              <el-tag :type="getDifficultyTag(currentQuestion?.difficulty)" size="small">
                {{ getDifficultyLabel(currentQuestion?.difficulty) }}
              </el-tag>
              <el-tag :type="getTypeTag(currentQuestion?.type)" size="small">
                {{ getTypeLabel(currentQuestion?.type) }}
              </el-tag>
              <el-tag :type="currentQuestion?.status === 1 ? 'success' : 'danger'" size="small">
                {{ currentQuestion?.status === 1 ? '正常' : '禁用' }}
              </el-tag>
            </div>
          </div>
        </template>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="题目ID">
            {{ currentQuestion?.id }}
          </el-descriptions-item>
          <el-descriptions-item label="所属分类">
            {{ getCategoryName(currentQuestion?.categoryId) }}
          </el-descriptions-item>
          <el-descriptions-item label="标签分类">
            {{ getTagName(currentQuestion?.tagCategoryId) }}
          </el-descriptions-item>
          <el-descriptions-item label="浏览次数">
            {{ currentQuestion?.viewCount }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentQuestion?.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(currentQuestion?.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 题目标题卡片 -->
      <el-card class="detail-card">
        <template #header>
          <div class="card-header">
            <span>题目标题</span>
          </div>
        </template>
        <div class="title-content">{{ currentQuestion?.title }}</div>
      </el-card>

      <!-- 题目内容卡片 -->
      <el-card class="detail-card">

        <template #header>
          <div class="card-header">
            <span>题目内容</span>
            <el-button
                v-if="!currentQuestion?.content"
                type="primary"
                link
                @click="handlerGetContentByCurrentQuesId()"
            >
              显示内容
            </el-button>
          </div>
        </template>
        <div v-if="currentQuestion?.content" class="rich-content editor-content-view" v-html="currentQuestion?.content"></div>
        <div v-else class="answer-placeholder">
          <el-icon class="lock-icon">
            <Lock/>
          </el-icon>
          <span>内容已隐藏，点击上方按钮显示</span>
        </div>
      </el-card>

      <!-- 题目答案卡片 -->
      <el-card class="detail-card">
        <template #header>
          <div class="card-header">
            <span>题目答案</span>
            <el-button
                v-if="!currentQuestion?.answer"
                type="primary"
                link
                @click="handleGetAnswerByQuestionId()"
            >
              显示答案
            </el-button>
          </div>
        </template>
        <div v-if="currentQuestion?.answer" class="rich-content editor-content-view" v-html="currentQuestion?.answer"></div>
        <div v-else class="answer-placeholder">
          <el-icon class="lock-icon">
            <Lock/>
          </el-icon>
          <span>答案已隐藏，点击上方按钮显示</span>
        </div>
      </el-card>
    </div>

    <!-- 底部操作栏 -->
    <template #footer>
      <div class="drawer-footer">
        <el-button @click="handleEdit" type="primary" :icon="Edit">
          编辑题目
        </el-button>
        <el-button @click="drawerVisible = false">关闭</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {ref, computed} from 'vue'
import {Edit, Lock} from '@element-plus/icons-vue'
import {useQuestionStore} from '../pinia/questionStore'
import type {IQuestion} from '../types/types'
import {handlerGetContentByCurrentQuesId} from "@/view/MainView/question/list/service/HandleGetContentByCurrentQuesId";
import {handleGetAnswerByQuestionId} from "@/view/MainView/question/list/service/HandleGetAnswerByQuestionId";

import "@/css/editor-content-view.css"

const questionStore = useQuestionStore()

// 抽屉可见性
const drawerVisible = computed({
  get: () => questionStore.detailVisible,
  set: (value) => {
    questionStore.setDetailVisible(value)
  }
})

// 当前题目数据
const currentQuestion = computed<IQuestion | null>(() => questionStore.currentQuestion)

// 难度配置
const difficultyConfig = {
  1: {label: '简单', type: 'success'},
  2: {label: '中等', type: 'warning'},
  3: {label: '困难', type: 'danger'}
}

// 类型配置
const typeConfig = {
  1: {label: '单选', type: 'primary'},
  2: {label: '多选', type: 'success'},
  3: {label: '判断', type: 'warning'},
  4: {label: '简答', type: 'info'}
}

// 获取难度标签
const getDifficultyLabel = (difficulty: number | undefined) => {
  return difficulty ? difficultyConfig[difficulty]?.label || '未知' : '未知'
}

const getDifficultyTag = (difficulty: number | undefined) => {
  return difficulty ? difficultyConfig[difficulty]?.type || '' : ''
}

// 获取类型标签
const getTypeLabel = (type: number | undefined) => {
  return type ? typeConfig[type]?.label || '未知' : '未知'
}

const getTypeTag = (type: number | undefined) => {
  return type ? typeConfig[type]?.type || '' : ''
}

// 获取分类名称
const getCategoryName = (categoryId: number | undefined) => {
  // TODO: 实现获取分类名称的逻辑
  return categoryId ? `分类${categoryId}` : '未知'
}

// 获取标签名称
const getTagName = (tagId: number | undefined) => {
  // TODO: 实现获取标签名称的逻辑
  return tagId ? `标签${tagId}` : '未知'
}

// 格式化日期时间
const formatDateTime = (datetime: string | undefined) => {
  return datetime ? new Date(datetime).toLocaleString() : '未知'
}

// 处理编辑
const handleEdit = () => {
  questionStore.formType = 'edit'
  questionStore.setFormVisible(true)
  drawerVisible.value = false
}
</script>

<style scoped>
.question-detail {
  padding: 12px;
  height: 100%;
  overflow-y: auto;
}

.detail-card {
  margin-bottom: 20px;
}

.detail-card:last-child {
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.header-tags {
  display: flex;
  gap: 8px;
}

.title-content {
  font-size: 16px;
  line-height: 1.6;
  color: var(--el-text-color-primary);
}

.rich-content {
  line-height: 1.6;
  color: var(--el-text-color-regular);
}

.rich-content :deep(img) {
  max-width: 100%;
  height: auto;
}

.rich-content :deep(pre) {
  background-color: var(--el-fill-color-light);
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
}

.answer-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: var(--el-text-color-secondary);
}

.lock-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.drawer-footer {
  padding: 16px 20px;
  text-align: right;
  border-top: 1px solid var(--el-border-color-lighter);
}

:deep(.el-descriptions__label) {
  font-weight: 500;
}

:deep(.el-card__header) {
  padding: 12px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

:deep(.el-descriptions) {
  padding: 8px 0;
}
</style> 