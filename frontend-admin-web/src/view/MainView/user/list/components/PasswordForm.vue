<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useUserListStore } from '../pinia/userListStore'
import type { IPasswordForm } from '../types/form'
import { passwordFormRules } from '../constants/formRules'
import { handlePasswordSubmit } from '../service/handlePassword'

// 获取 store 实例
const userListStore = useUserListStore()

const formRef = ref()
const form = ref<IPasswordForm>({
    id: 0,
    password: '',
    confirmPassword: ''
})

// 监听当前用户ID变化
const currentUserId = computed(() => userListStore.currentPasswordUser?.id || 0)
watch(
    currentUserId,
    (newId) => {
        form.value.id = newId
    },
    { immediate: true }
)

const rules = passwordFormRules

// 表单提交
const handleSubmit = async (): Promise<void> => {
    if (!formRef.value) return
    
    await formRef.value.validate(async (valid: boolean) => {
        if (valid) {
            await handlePasswordSubmit(form.value)
        }
    })
}

// 关闭弹窗
const handleClose = (): void => {
    formRef.value?.resetFields()
    userListStore.passwordFormVisible = false
}
</script>

<template>
  <el-dialog
    :model-value="userListStore.passwordFormVisible"
    title="修改密码"
    width="480px"
    @update:model-value="handleClose"
    :close-on-click-modal="false"
    class="password-form-dialog"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <div class="form-content">
        <div class="current-user">
          当前用户：{{ userListStore.currentPasswordUser?.username }}
        </div>
        <el-form-item label="新密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            show-password
          />
        </el-form-item>
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
.password-form-dialog :deep(.el-dialog) {
  margin-top: 15vh;
}

.password-form-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.form-content {
  padding: 20px;
}

.dialog-footer {
  padding: 16px 20px;
  border-top: 1px solid var(--el-border-color-lighter);
  text-align: right;
}

:deep(.el-dialog__header) {
  margin-right: 0;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.current-user {
    margin-bottom: 16px;
    color: var(--el-text-color-secondary);
    font-size: 14px;
}
</style> 