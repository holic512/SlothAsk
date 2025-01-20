<script setup>
import { ref, watch } from 'vue'

const visible = defineModel('visible')
const type = defineModel('type', {
  default: 'add'
})
const userData = defineModel('userData', {
  default: () => ({})
})

const formRef = ref(null)
const form = ref({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  status: 1,
  gender: 1,
  age: '',
  bio: '',
  avatar: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: type.value === 'add', message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: type.value === 'add', message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 监听编辑模式下的用户数据变化
watch(
  () => userData.value,
  (newVal) => {
    if (type.value === 'edit' && newVal) {
      const { password, confirmPassword, ...data } = newVal
      form.value = { ...form.value, ...data }
    }
  },
  { immediate: true }
)

// 是否设置密码（仅新增时有效）
const setPassword = ref(true)

const emit = defineEmits(['submit'])

// 表单提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate((valid) => {
    if (valid) {
      emit('submit', form.value)
    }
  })
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
    if (type.value === 'add') {
      setPassword.value = true
    }
  }
}

// 关闭弹窗
const handleClose = () => {
  resetForm()
  visible.value = false
}
</script>

<template>
  <el-dialog
    :model-value="visible"
    :title="type === 'add' ? '新增用户' : '编辑用户'"
    width="640px"
    @update:model-value="handleClose"
    class="user-form-dialog"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="70px"
      class="user-form"
    >
      <div class="form-section">
        <div class="section-content">
          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.nickname" placeholder="请输入昵称"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>
          </el-row>

          <template v-if="type === 'add'">

            <div style="margin-left:16px;">
              <el-form-item label=" " label-width="0">
                <el-checkbox v-model="setPassword" class="password-checkbox">设置初始密码</el-checkbox>
              </el-form-item>
            </div>


            <el-row :gutter="16" v-if="setPassword">
              <el-col :span="12">
                <el-form-item label="密码" prop="password">
                  <el-input
                      v-model="form.password"
                      type="password"
                      placeholder="请输入密码"
                      show-password
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="确认" prop="confirmPassword">
                  <el-input
                      v-model="form.confirmPassword"
                      type="password"
                      placeholder="请确认密码"
                      show-password
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </template>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="form.gender">
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="2">女</el-radio>
                  <el-radio :label="0">未知</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年龄" prop="age">
                <el-input-number v-model="form.age" :min="0" :max="150" style="width: 120px"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="1">正常</el-radio>
                  <el-radio :label="0">禁用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="头像">
                <el-upload
                    class="avatar-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="(res) => form.avatar = res.url"
                >
                  <img v-if="form.avatar" :src="form.avatar" class="avatar"/>
                  <div v-else class="avatar-placeholder">
                    <el-icon class="upload-icon">
                      <Plus/>
                    </el-icon>
                  </div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="简介" prop="bio">
            <el-input
                v-model="form.bio"
                type="textarea"
                :rows="2"
                placeholder="请输入个人简介"
            />
          </el-form-item>
        </div>
      </div>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.user-form-dialog :deep(.el-dialog) {
  margin-top: 5vh !important;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.user-form-dialog :deep(.el-dialog__body) {
  padding: 0;
  flex: 1;
  overflow-y: auto;
}

.user-form-dialog :deep(.el-dialog__header) {
  margin-right: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.user-form {
  padding: 0;
}

.form-section {
  margin-bottom: 8px;
  background-color: var(--el-bg-color);
}

.section-header {
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.section-title {
  font-size: 15px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  line-height: 1;
}

.section-content {
  padding: 20px;
}

.avatar-uploader {
  width: 80px;
  height: 80px;
  border: 1px dashed var(--el-border-color);
  border-radius: 4px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.upload-icon {
  font-size: 20px;
  color: var(--el-text-color-secondary);
}

.avatar {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: cover;
}

:deep(.el-form-item) {
  margin-bottom: 14px;
}

:deep(.el-form-item__label) {
  font-weight: 400;
  color: var(--el-text-color-regular);
}

.dialog-footer {
  padding: 16px 20px;
  border-top: 1px solid var(--el-border-color-lighter);
  text-align: right;
}

:deep(.el-input-number .el-input__wrapper) {
  padding-left: 11px;
  padding-right: 11px;
}

.password-checkbox {
  margin-bottom: 4px;
}
</style> 