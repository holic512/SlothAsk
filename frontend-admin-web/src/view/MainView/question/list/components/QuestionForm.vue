<script setup lang="ts">
import {ref, reactive, computed, watch, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import type {FormInstance, FormRules} from 'element-plus'
import {useQuestionStore} from '../pinia/questionStore'
import type {IQuestion, IQuestionForm, IQuestionSearchParams} from '../types/types'
import Editor from './editor.vue'
import {handleGetProjectList} from "@/view/MainView/question/list/service/HandleGetProjectList";
import {handleGetCateAndTagList} from "@/view/MainView/question/list/service/HandleGetCateAndTagList";
import {processContentBeforeSave} from "@/view/MainView/question/list/util/processContentBeforeSave";
import {apiAddQuestion} from "@/view/MainView/question/list/service/ApiAddQuestion";
import {handleGetAnswerByQuestionId} from "@/view/MainView/question/list/service/HandleGetAnswerByQuestionId";
import {handlerGetContentByCurrentQuesId} from "@/view/MainView/question/list/service/HandleGetContentByCurrentQuesId";
import {ApiPatchUpdateStatus} from "@/view/MainView/question/list/service/ApiPatchQuestion";
import {apiGetQuestionList} from "@/view/MainView/question/list/service/ApiGetQuestionList"; // 需要实现富文本编辑器组件

// 问题页面的 store
const questionStore = useQuestionStore()

const formRef = ref<FormInstance>()
const submitting = ref(false)

const activeName = ref('1')

// 表单数据
const defaultForm: IQuestionForm = {
  projectId: undefined,
  categoryId: undefined,
  title: '',
  content: '',
  answer: '',
  difficulty: 1,
  type: 4,
  tagCategoryId: undefined,
  status: 1,
}

const formData = reactive<IQuestionForm>({...defaultForm})

// 表单校验规则
const rules = reactive<FormRules>({
  title: [
    {required: true, message: '请输入题目标题', trigger: 'blur'},
    {min: 2, max: 500, message: '长度在 2 到 500 个字符', trigger: 'blur'}
  ],
  categoryId: [
    {required: true, message: '请选择所属分类', trigger: 'change'}
  ],
  content: [
    {required: true, message: '请输入题目内容', trigger: 'blur'}
  ],
  answer: [
    {required: true, message: '请输入题目答案', trigger: 'blur'}
  ],
  difficulty: [
    {required: true, message: '请选择题目难度', trigger: 'change'}
  ],
  type: [
    {required: true, message: '请选择题目类型', trigger: 'change'}
  ],
  tagCategoryId: [
    {required: true, message: '请选择标签分类', trigger: 'change'}
  ],
  projectId: [
    {required: true, message: '请选择项目分类', trigger: 'change'}
  ]
})

// 选项定义
const difficultyOptions = [
  {label: '简单', value: 1},
  {label: '中等', value: 2},
  {label: '困难', value: 3}
]

const typeOptions = [
  {label: '单选', value: 1, disabled: true},
  {label: '多选', value: 2, disabled: true},
  {label: '判断', value: 3, disabled: true},
  {label: '简答', value: 4}
]

// 对话框可见性
const dialogVisible = computed({
  get: () => questionStore.formVisible,
  set: (value) => {
    questionStore.setFormVisible(value)
    if (!value) {
      resetForm()
    }
  }
})

// 监听编辑数据
watch(
    () => [questionStore.currentQuestion, questionStore.formType],
    async (newVal) => {
      if (newVal && questionStore.formType === 'edit') {
        if (!questionStore.currentQuestion) return

        // 因为 content和 answer是独立获取 所以提前获取
        await handleGetAnswerByQuestionId()
        await handlerGetContentByCurrentQuesId()

        // 将当前数据赋值给 form
        Object.assign(formData, {
          projectId: questionStore.currentQuestion.projectId,
          categoryId: questionStore.currentQuestion.categoryId,
          title: questionStore.currentQuestion.title,
          content: questionStore.currentQuestion.content,
          answer: questionStore.currentQuestion.answer,
          difficulty: questionStore.currentQuestion.difficulty,
          type: questionStore.currentQuestion.type,
          tagCategoryId: questionStore.currentQuestion.tagCategoryIds,
          status: questionStore.currentQuestion.status
        })
      }else {
        Object.assign(formData, defaultForm)
        formRef.value?.resetFields()
      }
    }
)


// 重置表单
const resetForm = () => {
  // Object.assign(formData, defaultForm)
  // formRef.value?.resetFields()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    // 重构图片内容 - 移除 alt开头为 # 的 src
    formData.content = processContentBeforeSave(formData.content)
    formData.answer = processContentBeforeSave(formData.answer)

    // 构建提交数据
    const submitData = {
      ...formData,
      id: questionStore.currentQuestion?.id // 编辑时需要ID
    }

    // 判断 form 操作方式
    if (questionStore.formType === 'add') {
      await apiAddQuestion(submitData)
      ElMessage.success('添加成功')
    } else {

      // 确保 ID 存在
      if (!submitData.id) {
        ElMessage.error('更新失败，缺少题目 ID');
        return;
      }

      const data: IQuestion = {
        answer: submitData.answer,
        categoryId: submitData.categoryId,
        content: submitData.content,
        createTime: null,
        difficulty: submitData.difficulty,
        id: submitData.id,
        projectId: submitData.projectId,
        status: submitData.status,
        tagCategoryIds: submitData.tagCategoryId,
        title: submitData.title,
        type: submitData.type,
        updateTime: new Date().toISOString(),
        viewCount: null
      }
      await ApiPatchUpdateStatus(data);

      ElMessage.success('更新成功')
    }

    // 刷新内容
    const search: IQuestionSearchParams = {
      pageNum: questionStore.pageNum,
      pageSize: questionStore.pageSize
    }
    // 执行查询
    const response = await apiGetQuestionList(search)
    // 赋值给 table
    questionStore.tableData = response.data.records;
    questionStore.total = response.data.total;


    dialogVisible.value = false
  } catch (error) {
    ElMessage.error('操作失败，请检查表单')
  } finally {
    submitting.value = false
  }
}

// 当检测到 选中用户id时获取对应的数据
watch(() => formData.projectId, (newValue) => {
  if (newValue != undefined) {
    handleGetCateAndTagList(newValue);
  }
})

const emit = defineEmits(['success'])
</script>
<template>
  <el-dialog
      :title="questionStore.formType === 'add' ? '新增题目' : '编辑题目'"
      v-model="dialogVisible"
      width="80%"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"

  >
    <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="question-form"
    >
      <el-row :gutter="12">
        <!-- 左侧表单 -->
        <el-col :span="16">
          <!-- 标题 -->
          <el-form-item label="题目标题" prop="title">

            <el-input
                v-model="formData.title"
                placeholder="请输入题目标题"
                maxlength="100"
                show-word-limit
            />
          </el-form-item>

          <!-- 题目内容 -->
          <el-form-item label="问题编辑" prop="content">
            <el-collapse v-model="activeName" accordion style="width: 100%;height: 460px;margin-top: 8px">
              <el-collapse-item title="问题内容" name="1">
                <Editor v-model="formData.content" v-if="activeName === '1'"/>
              </el-collapse-item>

              <el-collapse-item title="问题答案" name="2">
                <Editor v-model="formData.answer" v-if="activeName === '2'"/>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>

        </el-col>

        <!-- 右侧表单 -->
        <el-col :span="8">

          <!-- 所属项目 -->
          <el-form-item label="所属项目" prop="projectId">
            <el-select
                v-model="formData.projectId"
                placeholder="请选择项目"
                class="w-full"
            >
              <el-option
                  v-for="item in questionStore.projectOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

          <!-- 所属分类 -->
          <el-form-item label="所属分类" prop="categoryId" v-show="formData.projectId!=undefined">
            <el-select
                v-model="formData.categoryId"
                placeholder="请选择分类"
                class="w-full"
            >
              <el-option
                  v-for="item in questionStore.categoryOptionsByProjectId"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

          <!-- 标签分类 -->
          <el-form-item label="标签分类" prop="tagCategoryId" v-show="formData.projectId!=undefined">
            <el-select
                v-model="formData.tagCategoryId"
                placeholder="请选择标签"
                class="w-full"
                multiple
            >
              <el-option
                  v-for="item in questionStore.tagOptionsByProjectId"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

          <!-- 题目难度 -->
          <el-form-item label="题目难度" prop="difficulty">
            <el-select
                v-model="formData.difficulty"
                placeholder="请选择难度"
                class="w-full"
            >
              <el-option
                  v-for="item in difficultyOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>

          <!-- 题目类型 -->
          <el-form-item label="题目类型" prop="type">
            <el-select
                v-model="formData.type"
                placeholder="请选择类型"
                class="w-full"
            >
              <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
              />
            </el-select>
          </el-form-item>


          <!-- 状态 -->
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :value="1">正常</el-radio>
              <el-radio :value="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>


<style scoped>
.question-form {
  padding: 20px;
}

.w-full {
  width: 100%;
}

</style> 