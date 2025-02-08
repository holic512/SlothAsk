<script setup lang="ts">
import { ref } from 'vue'
import type { RoleInfo } from '../types/role'
import type { FormInstance, FormRules } from 'element-plus'

const props = defineProps<{
  visible: boolean
  title: string
  formData: Partial<RoleInfo>
  loading: boolean
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  submit: []
}>()

const formRef = ref<FormInstance>()
const rules: FormRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
    { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9_-]+$/, message: '只能包含中文、字母、数字、下划线和横杠', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' },
    { max: 200, message: '长度不能超过200个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

const handleClose = () => {
  formRef.value?.resetFields()
  emit('update:visible', false)
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    emit('submit')
  } catch (error) {
    console.error('表单验证失败:', error)
    return false
  }
}
</script>

<template>
  <el-dialog
    :model-value="visible"
    :title="title"
    width="500px"
    @close="handleClose"
    class="role-dialog"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="80px"
      :disabled="loading"
    >
      <el-form-item label="角色名称" prop="name">
        <el-input 
          v-model="formData.name" 
          placeholder="请输入角色名称"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入角色描述"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose" :disabled="loading">取消</el-button>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleSubmit"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.role-dialog {
  :deep(.el-dialog__body) {
    padding: 20px 40px;
  }

  :deep(.el-form-item__label) {
    font-weight: normal;
  }

  :deep(.el-input),
  :deep(.el-select) {
    width: 100%;
  }

  :deep(.el-dialog__footer) {
    padding: 10px 20px 20px;
    border-top: 1px solid #dcdfe6;
  }

  :deep(.el-form-item__error) {
    padding-top: 4px;
  }
}

.dialog-footer {
  text-align: right;
}
</style> 