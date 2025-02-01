<script setup>
import { ref, watch } from 'vue'

const visible = defineModel('visible')  // 使用 defineModel 替代 props 和 emit

const props = defineProps({
  type: {
    type: String,
    default: 'add'  // 'add' 或 'edit'
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
    { required: true, message: '请输入项目描述', trigger: 'blur' },
    { max: 200, message: '最大长度为 200 个字符', trigger: 'blur' }
  ],
  creator_id: [
    { required: true, message: '请输入创建者ID', trigger: 'blur' }
  ],
  sort_order: [
    { required: true, message: '请输入排序序号', trigger: 'blur' },
    { type: 'number', message: '排序序号必须为数字', trigger: 'blur' }
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
  visible.value = false  // 直接修改 visible.value
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
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="项目名称" prop="project_name">
        <el-input
          v-model="formData.project_name"
          placeholder="请输入项目名称"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="项目描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入项目描述"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="创建者ID" prop="creator_id">
        <el-input
          v-model="formData.creator_id"
          placeholder="请输入创建者ID"
        />
      </el-form-item>

      <el-form-item label="排序序号" prop="sort_order">
        <el-input-number
          v-model="formData.sort_order"
          :min="0"
          :max="9999"
          controls-position="right"
        />
      </el-form-item>

      <el-form-item label="状态">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-input-number) {
  width: 100%;
}
</style>
