<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Clock } from '@element-plus/icons-vue'

// 登录表单数据
const loginType = ref('email') // email 或 account
const form = reactive({
  email: '',
  account: '',
  password: '',
  captchaId: '',
  captchaAnswer: ''
})

// 时钟数据
const currentTime = ref(new Date().toLocaleTimeString())
setInterval(() => {
  currentTime.value = new Date().toLocaleTimeString()
}, 1000)

// 切换登录方式
const toggleLoginType = () => {
  loginType.value = loginType.value === 'email' ? 'account' : 'email'
  form.email = ''
  form.account = ''
  form.password = ''
}

// 登录处理
const handleLogin = () => {
  // TODO: 实现登录逻辑
  ElMessage.success('登录成功')
}
</script>

<template>
  <div class="login-container">
    <div class="login-background"></div>
    <!-- 左侧登录区域 -->
    <div class="login-left">
      <div class="login-header">
        <h1 class="title">SlothAsk</h1>
        <p class="subtitle">管理员登录系统</p>
      </div>

      <div class="login-form">
        <h2 class="form-title">{{ loginType === 'email' ? '邮箱登录' : '账号登录' }}</h2>
        
        <!-- 邮箱登录表单 -->
        <template v-if="loginType === 'email'">
          <el-form :model="form" label-position="top">
            <el-form-item label="邮箱">
              <el-input 
                v-model="form.email"
                placeholder="请输入邮箱"
                prefix-icon="Message"
              />
            </el-form-item>
            <el-form-item label="密码">
              <el-input 
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
          </el-form>
        </template>

        <!-- 账号登录表单 -->
        <template v-else>
          <el-form :model="form" label-position="top">
            <el-form-item label="账号">
              <el-input 
                v-model="form.account"
                placeholder="请输入账号"
                prefix-icon="User"
              />
            </el-form-item>
            <el-form-item label="密码">
              <el-input 
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
          </el-form>
        </template>

        <!-- 登录按钮 -->
        <el-button 
          type="primary" 
          class="login-button"
          @click="handleLogin"
        >
          登录
        </el-button>

        <!-- 切换登录方式 -->
        <div class="login-switch">
          <el-link 
            type="primary" 
            @click="toggleLoginType"
          >
            切换至{{ loginType === 'email' ? '账号密码' : '邮箱' }}登录
          </el-link>
        </div>
      </div>
    </div>

    <!-- 右侧装饰区域 -->
    <div class="login-right">
      <div class="time-display">
        <el-icon class="clock-icon"><Clock /></el-icon>
        <span class="time-text">{{ currentTime }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  position: relative;
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('/login/loginBc.jpg') center center;
  background-size: cover;
  background-repeat: no-repeat;
  z-index: 0;
}

.login-left {
  position: relative;
  flex: 0.3;
  padding: 40px;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(8px);
  z-index: 1;
}

.login-header {
  margin-bottom: 60px;
}

.title {
  font-size: 36px;
  color: #303133;
  margin: 0;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.subtitle {
  color: #606266;
  margin-top: 8px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.login-form {
  max-width: 300px;
  margin: 0 auto;
  width: 100%;
  padding: 30px;
}

.form-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 30px;
  font-weight: 500;
  text-align: center;
}

.login-button {
  width: 100%;
  height: 44px;
  margin-top: 20px;
  font-size: 16px;
}

.login-switch {
  margin-top: 16px;
  text-align: center;
}

.login-right {
  position: relative;
  flex: 0.7;
  z-index: 1;
}

.time-display {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  text-align: center;
  font-size: 32px;
  display: flex;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, 
    rgba(255, 255, 255, 0.1) 0%, 
    rgba(255, 255, 255, 0.05) 100%);
  padding: 25px 40px;
  border-radius: 20px;
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.clock-icon {
  font-size: 32px;
  color: rgba(255, 255, 255, 0.9);
}

.time-text {
  font-family: 'Monaco', monospace;
  font-weight: 300;
  letter-spacing: 3px;
  color: rgba(255, 255, 255, 0.9);
}

:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.3) !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  box-shadow: none !important;
}

:deep(.el-input__wrapper:hover) {
  background: rgba(255, 255, 255, 0.4) !important;
}

:deep(.el-form-item__label) {
  color: #303133 !important;
  font-weight: 500;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #409EFF 0%, #007FFF 100%);
  border: none;
  transition: transform 0.3s ease;
}

:deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}
</style>