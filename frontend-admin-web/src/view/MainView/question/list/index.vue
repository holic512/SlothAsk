<script setup lang="ts">
import {onMounted} from 'vue'
import SearchForm from './components/SearchForm.vue'
import QuestionTable from './components/QuestionTable.vue'
import QuestionForm from './components/QuestionForm.vue'
import QuestionDetail from './components/QuestionDetail.vue'
import {useQuestionStore} from './pinia/questionStore'

import {ElMessage} from 'element-plus'
import {handleGetProjectList} from "@/view/MainView/question/list/service/HandleGetProjectList";
import {handleGetAllCateAndTag} from "@/view/MainView/question/list/service/HandleGetAllCateAndTag";

const questionStore = useQuestionStore()

// 组件挂载时初始化数据
onMounted(async () => {
  await handleGetProjectList()
  await handleGetAllCateAndTag()
})
</script>

<template>
  <div class="question-list-container">
    <!-- 搜索表单 -->
    <SearchForm/>

    <!-- 题目列表表格 -->
    <QuestionTable/>

  </div>

  <!-- 题目表单（新增/编辑） -->
  <QuestionForm/>

  <!-- 题目详情 -->
  <QuestionDetail/>
</template>

<style scoped>
.question-list-container {
  padding: 16px;
  background-color: var(--el-bg-color);
  border-radius: 4px;
}

</style> 