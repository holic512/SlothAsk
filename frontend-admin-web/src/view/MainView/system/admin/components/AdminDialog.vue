<!--./admin/components/AdminDialog.vue -->
<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { AdminUser } from '../types/admin'
import type { RoleInfo } from '../../roles/types/role'


const props = defineProps<{
  visible: boolean
  title: string

  formData: Partial<AdminUser>
  roleList: RoleInfo[]
  loading?: boolean
}>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  'submit': [data: Partial<AdminUser>]
}>()

const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

const formRef = ref<FormInstance>()
const form = ref<Partial<AdminUser>>({})

// 表单验证规则
const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  real_name: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  role_id: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 监听表单数据变化
watch(
  () => props.formData,
  (val) => {
    form.value = { ...val }
  },
  { immediate: true, deep: true }
)

const handleClose = () => {
  dialogVisible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  emit('submit', form.value)
}
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="title"
    width="500px"
    @update:model-value="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="form.username"
          placeholder="请输入用户名"
          :disabled="!!form.id"
        />
      </el-form-item>

      <el-form-item
        label="密码"
        prop="password"
        :rules="form.id ? [] : rules.password"
      >
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="真实姓名" prop="real_name">
        <el-input
          v-model="form.real_name"
          placeholder="请输入真实姓名"
        />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="form.email"
          placeholder="请输入邮箱"
        />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="form.phone"
          placeholder="请输入手机号"
        />
      </el-form-item>

      <el-form-item label="角色" prop="role_id">
        <el-select
          v-model="form.role_id"
          placeholder="请选择角色"
        >
          <el-option
            v-for="role in roleList"
            :key="role.id"
            :label="role.name"
            :value="role.id"
          >
            <span>{{ role.name }}</span>
            <span class="role-description">{{ role.description }}</span>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-switch
          v-model="form.status"
          :active-value="1"
          :inactive-value="0"
          inline-prompt
          active-text="启用"
          inactive-text="禁用"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button
        type="primary"
        :loading="loading"
        @click="handleSubmit"
      >
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.dialog-form {
  padding: 20px 20px 0;
}

.dialog-footer {
  padding: 20px 0 0;
  text-align: right;
}

:deep(.el-input),
:deep(.el-select) {
  width: 220px;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

.role-description {
  margin-left: 8px;
  color: #999;
  font-size: 13px;
}
</style> 