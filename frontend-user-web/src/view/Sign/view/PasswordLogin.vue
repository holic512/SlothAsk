<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Message, Lock, View, Hide } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { useSessionStore } from "@/pinia/Session";
import axios from "@/axios/axios";

const router = useRouter();
const route = useRoute();
const account = ref('');
const password = ref('');
const loading = ref(false);
const showPassword = ref(false);

// 登录
const handleLogin = async () => {
  if (!account.value || !password.value) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  
  loading.value = true;
  try {
    const response = await axios.post('service-user/user/sign/passwordLogin', {
      account: account.value,
      password: password.value
    });

    if (response.data.status === 200) {
      ElMessage.success('登录成功');
      
      // 保存token
      const store = useSessionStore();
      store.setSession(response.data.data);
      
      // 获取重定向地址，如果没有则跳转到首页
      const redirectPath = route.query.redirect || '/';
      router.push(redirectPath);
    } else {
      ElMessage.error(response.data.message);
    }
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error(error.response?.data?.message || '登录失败');
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
      <!-- 账号输入 -->
      <el-form-item>
        <el-input
          v-model="account"
          placeholder="请输入邮箱或用户名"
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
      <div class="switch-login-type">
        <a @click="switchToEmailLogin">
          <el-icon><Message /></el-icon>
          验证码登录
        </a>
        <a @click="handleForgotPassword" class="forgot-password">
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
  animation: fadeIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.08);
  padding: 0 16px;
  transition: all 0.2s ease;
  background-color: #fff;
  height: 44px;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.15);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.2);
}

.custom-input :deep(.el-input__inner) {
  color: #1a1a1a;
  font-size: 14px;
  font-weight: 400;
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: #999;
  font-weight: 400;
}

.input-icon {
  color: #666;
  font-size: 16px;
  margin-right: 6px;
}

.password-toggle {
  color: #666;
  font-size: 16px;
  cursor: pointer;
  transition: color 0.2s ease;
}

.password-toggle:hover {
  color: #1a1a1a;
}

.login-btn {
  width: 100%;
  border-radius: 12px;
  height: 44px;
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: all 0.2s ease;
  margin-top: 8px;
  background-color: #1a1a1a;
  color: #fff;
  border: none;
}

.login-btn:hover {
  transform: translateY(-1px);
  background-color: #000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.switch-login-type {
  text-align: center;
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}

.switch-login-type a {
  color: #666;
  text-decoration: none;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  border-radius: 10px;
  background-color: #f5f5f5;
}

.switch-login-type a:hover {
  color: #1a1a1a;
  background-color: #eee;
}

.forgot-password {
  color: #666 !important;
  text-decoration: none;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  border-radius: 10px;
  background-color: #f5f5f5;
}

.forgot-password:hover {
  color: #1a1a1a !important;
  background-color: #eee;
}

.el-form-item {
  margin-bottom: 16px;
}

.el-form-item:last-child {
  margin-bottom: 0;
}
</style>