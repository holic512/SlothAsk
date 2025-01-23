<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { useUserListStore } from '../pinia/userListStore'
import type { IUserForm } from '../types/form'
import type { IUser } from '../types/user'
import { getUserFormRules } from '../constants/formRules'
import { handleUserFormSubmit } from '../service/handleFormSubmit'

// 获取 store 实例
const userListStore = useUserListStore()

// 表单引用
const formRef = ref()

// 创建本地表单数据
const formData = ref<IUserForm>({
    username: '',
    nickname: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    status: 1,
    gender: 1,
    age: undefined,
    bio: '',
    avatar: ''
})

// 监听编辑用户数据变化
watch(
    () => userListStore.currentEditUser,
    (newUser: IUser | null) => {
        if (userListStore.formType === 'edit' && newUser) {
            // 编辑模式：使用当前编辑用户的数据
            formData.value = {
                ...newUser,
                password: '',
                confirmPassword: ''
            }
        } else {
            // 新增模式：重置为默认值
            formData.value = {
                username: '',
                nickname: '',
                email: '',
                phone: '',
                password: '',
                confirmPassword: '',
                status: 1,
                gender: 1,
                age: undefined,
                bio: '',
                avatar: ''
            }
        }
    },
    { immediate: true }
)

// 状态选项
const statusOptions = [
    { value: 1, label: '正常' },
    { value: 0, label: '禁用' }
]

// 性别选项
const genderOptions = [
    { value: 1, label: '男' },
    { value: 2, label: '女' },
    { value: 0, label: '保密' }
]

// 获取表单校验规则
const rules = getUserFormRules(userListStore.formType)

// 根据表单类型显示不同的表单项
const showPasswordFields = computed(() => userListStore.formType === 'add')

// 表单提交
const handleSubmit = async (): Promise<void> => {
    if (!formRef.value) return
    
    await formRef.value.validate(async (valid: boolean) => {
        if (valid) {
            await handleUserFormSubmit(formData.value)
        }
    })
}

// 重置表单
const resetForm = (): void => {
    if (formRef.value) {
        formRef.value.resetFields()
    }
}

// 关闭弹窗
const handleClose = (): void => {
    resetForm()
    userListStore.formVisible = false
}
</script>

<template>
  <el-dialog
    :model-value="userListStore.formVisible"
    :title="userListStore.formType === 'add' ? '新增用户' : '编辑用户'"
    width="640px"
    @update:model-value="handleClose"
    class="user-form-dialog"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="70px"
      class="user-form"
    >
      <div class="form-section">
        <div class="section-content">
          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="formData.username" placeholder="请输入用户名"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="formData.nickname" placeholder="请输入昵称"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="formData.email" placeholder="请输入邮箱"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="formData.phone" placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>
          </el-row>

          <template v-if="showPasswordFields">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="密码" prop="password">
                  <el-input 
                    v-model="formData.password" 
                    type="password" 
                    placeholder="请输入密码"
                    show-password
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input 
                    v-model="formData.confirmPassword" 
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
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="formData.status">
                  <el-radio 
                    v-for="option in statusOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="formData.gender">
                  <el-radio 
                    v-for="option in genderOptions"
                    :key="option.value"
                    :value="option.value"
                  >
                    {{ option.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="年龄" prop="age">
                <el-input-number v-model="formData.age" :min="0" :max="150" style="width: 120px"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="头像">
                <el-upload
                    class="avatar-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="(res) => formData.avatar = res.url"
                >
                  <img v-if="formData.avatar" :src="formData.avatar" class="avatar"/>
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
                v-model="formData.bio"
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