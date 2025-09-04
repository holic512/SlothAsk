<template>
  <el-dialog
    v-model="visible"
    :before-close="handleClose"
    center
    title="登录提示"
    width="400px"
  >
    <div class="login-prompt-content">
      <el-icon class="warning-icon" size="48">
        <Warning />
      </el-icon>
      <p class="prompt-text">
        您还未登录，无法使用此功能。
      </p>
      <p class="sub-text">
        请先登录后再进行操作。
      </p>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleGoToLogin">
          去登录
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {computed} from 'vue'
import {ElButton, ElDialog, ElIcon} from 'element-plus'
import {Warning} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'

const props = defineProps<{
  modelValue: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const router = useRouter()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const handleClose = () => {
  visible.value = false
}

const handleGoToLogin = () => {
  visible.value = false
  // 跳转到登录页面
  router.push('/sign/email?redirect=/jobrecruit')
}
</script>

<style scoped>
.login-prompt-content {
  text-align: center;
  padding: 20px 0;
}

.warning-icon {
  color: #e6a23c;
  margin-bottom: 16px;
}

.prompt-text {
  font-size: 16px;
  color: #303133;
  margin: 0 0 8px 0;
  font-weight: 500;
}

.sub-text {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.dialog-footer {
  text-align: center;
}

.dialog-footer .el-button {
  margin: 0 8px;
}
</style>