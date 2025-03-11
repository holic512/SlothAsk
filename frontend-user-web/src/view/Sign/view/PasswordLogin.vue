<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Message, Lock, View, Hide } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const router = useRouter();
const route = useRoute();
const email = ref('');
const password = ref('');
const loading = ref(false);
const showPassword = ref(false);

// 登录
const handleLogin = async () => {
  if (!email.value || !password.value) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  
  loading.value = true;
  try {
    // TODO: 调用登录API
    ElMessage.success('登录成功');
    
    // 获取重定向地址，如果没有则跳转到首页
    const redirectPath = route.query.redirect || '/';
    router.push(redirectPath);
  } catch (error) {
    ElMessage.error('登录失败');
  } finally {
    loading.value = false;
  }
};

// 切换到邮箱验证码登录
const switchToEmailLogin = () => {
  router.push({
    path: '/sign/email',
    query: route.query // 保持重定向参数
  });
};

// 忘记密码
const handleForgotPassword = () => {
  // TODO: 实现忘记密码功能
  ElMessage.info('忘记密码功能即将上线');
};

// 切换密码显示状态
const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};
</script>

<template>
  <div class="password-login">
    <el-form @submit.prevent="handleLogin">
      <!-- 邮箱输入 -->
      <el-form-item>
        <el-input
          v-model="email"
          placeholder="请输入邮箱"
          type="email"
          size="large"
          class="custom-input"
          :autofocus="false"
        >
          <template #prefix>
            <el-icon class="input-icon"><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 密码输入 -->
      <el-form-item>
        <el-input
          v-model="password"
          :type="showPassword ? 'text' : 'password'"
          placeholder="请输入密码"
          size="large"
          class="custom-input"
          :autofocus="false"
        >
          <template #prefix>
            <el-icon class="input-icon"><Lock /></el-icon>
          </template>
          <template #suffix>
            <el-icon class="input-icon clickable" @click="togglePasswordVisibility">
              <component :is="showPassword ? View : Hide" />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 登录按钮 -->
      <el-form-item>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleLogin"
          size="large"
          class="login-btn"
        >
          登录
        </el-button>
      </el-form-item>

      <!-- 底部操作区 -->
      <div class="bottom-actions">
        <a @click="switchToEmailLogin" class="action-link">
          <el-icon><Message /></el-icon>
          验证码登录
        </a>
        <a @click="handleForgotPassword" class="action-link">
          <el-icon><QuestionFilled /></el-icon>
          忘记密码？
        </a>
      </div>
    </el-form>
  </div>
</template>

<style scoped>
.password-login {
  width: 100%;
  animation: fadeIn 0.5s ease;
  user-select: none;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-title {
  text-align: center;
  margin-bottom: 24px;
  color: #2c3e50;
  font-weight: 500;
  font-size: 20px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px rgba(220, 223, 230, 0.3), 0 2px 8px rgba(0, 0, 0, 0.02);
  padding: 0 15px;
  transition: all 0.3s ease;
  background-color: #fff;
  user-select: text;
  height: 48px;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px rgba(192, 196, 204, 0.5), 0 4px 12px rgba(0, 0, 0, 0.04);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.5), 0 4px 12px rgba(0, 0, 0, 0.08);
  border-color: #000;
}

.custom-input :deep(.el-input__inner) {
  user-select: text;
  font-size: 15px;
}

.input-icon {
  color: #606266;
  font-size: 18px;
  margin-right: 5px;
}

.clickable {
  cursor: pointer;
  transition: all 0.2s ease;
}

.clickable:hover {
  color: #000;
  transform: scale(1.1);
}

.login-btn {
  width: 100%;
  border-radius: 8px;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  user-select: none;
  margin-top: 10px;
  background-color: #000;
  color: #fff;
  border: none;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
  background-color: #222;
}

.bottom-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
  user-select: none;
}

.action-link {
  color: #606266;
  text-decoration: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 8px 12px;
  border-radius: 20px;
  background-color: rgba(0, 0, 0, 0.05);
}

.action-link:hover {
  color: #000;
  background-color: rgba(0, 0, 0, 0.08);
}

.action-link i {
  font-size: 14px;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>