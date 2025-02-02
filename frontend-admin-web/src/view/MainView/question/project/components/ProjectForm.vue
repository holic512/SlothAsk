<script setup>
import { ref, watch } from 'vue'
import { QuestionFilled } from '@element-plus/icons-vue'

const visible = defineModel('visible')  

const props = defineProps({
  type: {
    type: String,
    default: 'add'
  },
  data: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['submit'])

// 表单数据
const formData = ref({
  project_name: '',
  description: '',
  creator_id: '',
  sort_order: 0,
  status: 1
})

// 表单规则
const rules = {
  project_name: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { max: 200, message: '最大长度为 200 个字符', trigger: 'blur' }
  ],
  creator_id: [
    { required: true, message: '请输入创建者ID', trigger: 'blur' }
  ],
  sort_order: [
    { required: true, message: '请输入权重', trigger: 'blur' },
    { type: 'number', message: '权重必须为数字', trigger: 'blur' }
  ]
}

const formRef = ref(null)

// 监听编辑数据变化
watch(
  () => props.data,
  (newVal) => {
    if (props.type === 'edit' && newVal) {
      // 编辑模式下，用传入的数据初始化表单
      formData.value = { ...newVal }
    } else {
      // 新增模式下，重置表单
      formData.value = {
        project_name: '',
        description: '',
        creator_id: '',
        sort_order: 0,
        status: 1
      }
    }
  },
  { immediate: true }
)

// 重置表单
const resetForm = () => {
  formData.value = {
    project_name: '',
    description: '',
    creator_id: '',
    sort_order: 0,
    status: 1
  }
  formRef.value?.resetFields()
}

// 关闭弹窗
const handleClose = () => {
  visible.value = false 
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('submit', { ...formData.value })
      handleClose()
    }
  })
}
</script>

<template>
  <el-dialog
    :title="type === 'add' ? '新增项目' : '编辑项目'"
    v-model="visible"
    width="650px"
    :close-on-click-modal="false"
    @close="handleClose"
    class="project-form-dialog"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      label-position="right"
      class="project-form"
    >
      <div class="form-section">
        <div class="section-title">基本信息</div>
        <el-form-item label="项目名称" prop="project_name">
          <el-input
            v-model="formData.project_name"
            placeholder="请输入项目名称"
            maxlength="50"
            show-word-limit
            clearable
          />
        </el-form-item>

        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入项目描述（选填）"
            maxlength="200"
            show-word-limit
            resize="none"
          />
        </el-form-item>
      </div>

      <div class="form-section">
        <div class="section-title">配置信息</div>
        <div class="config-row">
          <el-form-item label="权重" prop="sort_order" class="weight-item">
            <el-tooltip
              content="权重值越大，排序越靠前"
              placement="top"
              effect="light"
            >
              <div class="weight-wrapper">
                <el-input-number
                  v-model="formData.sort_order"
                  :min="0"
                  :max="9999"
                  controls-position="right"
                  class="weight-input"
                />
                <el-icon class="question-icon"><QuestionFilled /></el-icon>
              </div>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="状态" class="status-item">
            <el-radio-group v-model="formData.status">
              <el-radio :label="1">
                <el-tag size="small" type="success" effect="plain">正常</el-tag>
              </el-radio>
              <el-radio :label="0">
                <el-tag size="small" type="danger" effect="plain">禁用</el-tag>
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
      </div>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose" plain>取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.project-form-dialog :deep(.el-dialog__body) {
  padding: 20px 24px;
}

.project-form {
  margin: 0;
}

.form-section {
  background-color: var(--el-fill-color-blank);
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.form-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.section-title::before {
  content: '';
  width: 4px;
  height: 16px;
  background-color: var(--el-color-primary);
  margin-right: 8px;
  border-radius: 2px;
}

.weight-item {
  display: flex;
  align-items: center;
}

.weight-item :deep(.el-form-item__content) {
  flex: 1;
  margin-left: 0 !important;
}

.weight-input {
  width: 100%;
}

.status-item :deep(.el-radio-group) {
  display: flex;
  gap: 24px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 8px;
}

:deep(.el-tag) {
  margin-left: 4px;
}

:deep(.el-input-number .el-input__wrapper) {
  padding-left: 12px;
  box-sizing: border-box;
}

:deep(.el-textarea__inner) {
  font-family: inherit;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

.config-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  align-items: flex-start;
}

.weight-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.question-icon {
  color: var(--el-text-color-secondary);
  font-size: 16px;
  cursor: help;
}

.question-icon:hover {
  color: var(--el-color-primary);
}
</style>
