<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import {Search, Plus, Delete, Refresh, Watch} from '@element-plus/icons-vue'
import {useQuestionStore} from '../pinia/questionStore'
import type {IQuestionSearchParams} from '../types/types'
import {handleGetProjectList} from "@/view/MainView/question/list/service/HandleGetProjectList";
import {handleGetCateAndTagList} from "@/view/MainView/question/list/service/HandleGetCateAndTagList";
import {apiGetQuestionList} from "@/view/MainView/question/list/service/ApiGetQuestionList";
import {ElMessage, ElMessageBox} from "element-plus";
import {apiDeleteQuestion} from "@/view/MainView/question/list/service/ApiDeleteQuestion";
import {apiDeleteQuestions} from "@/view/MainView/question/list/service/ApiDeleteQuestions";
import {refreshData} from "@/view/MainView/question/list/service/refreshData";

// 问题页面的 store
const questionStore = useQuestionStore()

// 搜索表单
const searchForm = ref<IQuestionSearchParams>({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  projectId: undefined,
  categoryId: undefined,
  difficulty: undefined,
  type: undefined,
  status: undefined
})

// 选项定义
const difficultyOptions = [
  {label: '简单', value: 1},
  {label: '中等', value: 2},
  {label: '困难', value: 3}
]

const typeOptions = [
  {label: '单选', value: 1},
  {label: '多选', value: 2},
  {label: '判断', value: 3},
  {label: '简答', value: 4}
]

const statusOptions = [
  {label: '正常', value: 1},
  {label: '禁用', value: 0}
]

// 处理新增
const handleAdd = () => {
  questionStore.formType = 'add'
  questionStore.setCurrentQuestion(null)
  questionStore.setFormVisible(true)
}

// 批量删除
const handleBatchDelete = async () => {
  if (questionStore.selectedRows.length === 0) {
    ElMessage.warning('请先选择至少一个题目');
    return;
  }

  try {
    // 确认是否删除选中的题目
    await ElMessageBox.confirm(
        '确定要删除选中的题目吗？',
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    );

    // 获取选中的 ID
    const idsToDelete = questionStore.selectedRows.map(row => row.id);
    const deleteCount = idsToDelete.length; // 记录删除的数量

    // 执行批量删除 API 调用
    await apiDeleteQuestions(idsToDelete);

    // 删除成功提示
    ElMessage.success('删除成功');

    // 计算新 total
    let newTotal = questionStore.total - deleteCount;
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
      // 删除失败提示
      ElMessage.error('删除失败');
    }
  }
};


// 在页面渲染时获取 项目列表 当选中项目时 显示其项目下的分类和标签
onMounted(async () => {
  await handleGetProjectList()
})

// 当检测到 选中用户id时获取对应的数据
watch(() => searchForm.value.projectId, (newValue) => {
  if (newValue != undefined) {
    handleGetCateAndTagList(newValue);
  }
})

// 监听搜索参数改变
watch(searchForm, async (newValue) => {

  
      // 计算最大可用页数
      const maxPage = Math.ceil(questionStore.total / newValue.pageSize);
      // 如果当前页码无效（超出最大页数），则重置为 1 或 maxPage
      if (newValue.pageSize * (newValue.pageNum - 1) >= questionStore.total) {
        questionStore.pageNum = maxPage > 0 ? maxPage : 1;
      }

      // 执行查询
      const response = await apiGetQuestionList(newValue)

      // 赋值给 table
      questionStore.tableData = response.data.records;
      questionStore.total = response.data.total;
    },
    {
      deep: true,
      immediate: true
    })

// 监听翻页 - 翻页后 修改搜索数据 触发上面的 代码
watch(() => [questionStore.pageSize, questionStore.pageNum], (newValue) => {
  [searchForm.value.pageSize, searchForm.value.pageNum] = newValue
})


</script>
<template>
  <div class="search-form">
    <el-form :model="searchForm" ref="formRef" :inline="true">
      <el-form-item>
        <el-input
            v-model="searchForm.keyword"
            placeholder="请输入题目信息进行搜索"
            clearable
            style="width: 240px"
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-select
            v-model="searchForm.projectId"
            placeholder="选择项目"
            style="width: 100px"
            clearable
        >
          <el-option
              v-for="item in questionStore.projectOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>


      <el-form-item v-if="searchForm.projectId">
        <el-select
            v-model="searchForm.categoryId"
            placeholder="选择分类"
            style="width: 100px"
            clearable
        >
          <el-option
              v-for="item in questionStore.categoryOptionsByProjectId"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </el-form-item>

      <!--      <el-form-item v-if="searchForm.projectId">-->
      <!--        <el-select-->
      <!--            v-model="searchForm.tagCategoryId"-->
      <!--            placeholder="选择标签"-->
      <!--            style="width: 100px"-->
      <!--            clearable-->
      <!--        >-->
      <!--          <el-option-->
      <!--              v-for="item in questionStore.tagOptions"-->
      <!--              :key="item.id"-->
      <!--              :label="item.name"-->
      <!--              :value="item.id"-->
      <!--          />-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->

      <el-form-item>
        <el-select
            v-model="searchForm.difficulty"
            placeholder="选择难度"
            style="width: 100px"
            clearable
        >
          <el-option
              v-for="item in difficultyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-select
            v-model="searchForm.type"
            placeholder="选择类型"
            style="width: 100px"
            clearable
        >
          <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>


      <el-form-item>
        <el-select
            v-model="searchForm.status"
            placeholder="选择状态"
            style="width: 100px"
            clearable
        >
          <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>


    </el-form>

    <div class="operation-buttons">
      <el-button type="primary" :icon="Plus" @click="handleAdd">
        新增题目
      </el-button>
      <el-button
          type="danger"
          :icon="Delete"
          :disabled="!questionStore.selectedRows.length"
          @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>
  </div>
</template>


<style scoped>
.search-form {
  display: flex;
  justify-content: space-between; /* 左右分布 */
}

.operation-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 4px;
}
</style>