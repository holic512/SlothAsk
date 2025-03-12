<script setup lang="ts">
import {ref, onMounted} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import {Message, Key, Lock} from '@element-plus/icons-vue';
import {ElMessage} from 'element-plus';
import SliderCaptcha from '../../../components/SliderCaptcha.vue';
import axios from 'axios';
import {apiSendSignVerificationCode} from "@/view/Sign/service/ApiSendSignVerificationCode";
import {ApiVerifySignVerificationCode} from "@/view/Sign/service/ApiVerifySignVerificationCode";

const router = useRouter();
const route = useRoute();
const email = ref('');
const verificationCode = ref('');
const countdown = ref(0);
const loading = ref(false);

// 滑块验证码相关
const showCaptchaDialog = ref(false);
const captchaVerified = ref(false);

// 发送验证码
const sendVerificationCode = async () => {
  if (!email.value) {
    ElMessage.warning('请输入邮箱地址');
    return;
  }

  // 如果未通过验证，显示滑块验证码
  if (!captchaVerified.value) {
    showCaptchaDialog.value = true;
    return;
  }

  // 已通过验证，直接发送验证码
  await sendEmailCode();
};

// 验证码验证成功后的回调
const handleCaptchaSuccess = (uid: string) => {
  captchaVerified.value = true;
  showCaptchaDialog.value = false;

  // 验证成功后自动发送验证码
  setTimeout(() => {
    sendEmailCode();
  }, 500);
};

// 关闭验证码弹窗
const closeCaptcha = () => {
  showCaptchaDialog.value = false;
};

// 实际发送邮箱验证码的函数
const sendEmailCode = async () => {
  loading.value = true;
  try {
    // 调用发送验证码API
    const response = await apiSendSignVerificationCode(email.value)


    if (response.status === 200) {
      ElMessage.success('验证码已发送，请查收邮箱');
      countdown.value = 60;
      const timer = setInterval(() => {
        countdown.value--;
        if (countdown.value <= 0) {
          clearInterval(timer);
          // 倒计时结束后重置验证状态，下次需要重新验证
          captchaVerified.value = false;
        }
      }, 1000);
    } else {
      ElMessage.error(response.data.message || '发送验证码失败');
      captchaVerified.value = false;
    }
  } catch (error) {
    console.error('发送验证码失败:', error);
    ElMessage.error('发送验证码失败，请稍后重试');
    captchaVerified.value = false;
  } finally {
    loading.value = false;
  }
};

// 登录
const handleLogin = async () => {
  if (!email.value || !verificationCode.value) {
    ElMessage.warning('请填写完整信息');
    return;
  }

  loading.value = true;
  try {
    // 调用登录API
    const response = await ApiVerifySignVerificationCode(email.value, verificationCode.value);

    if (response.status === 200) {
      ElMessage.success('登录成功');

      // 获取重定向地址，如果没有则跳转到首页
      const redirectPath = Array.isArray(route.query.redirect)
          ? route.query.redirect[0]
          : (route.query.redirect || '/');

      // 确保redirectPath不为null
      await router.push(redirectPath ? redirectPath : '/');
    } else if (response.status === 201) {
      // 验证成功但用户未注册，跳转到注册页面
      ElMessage.info('请完成注册');
      
      // 获取重定向地址
      const redirectPath = Array.isArray(route.query.redirect)
          ? route.query.redirect[0]
          : (route.query.redirect || '/');
      
      router.push({
        path: '/sign/register',
        query: {
          email: email.value,           // 传递邮箱
          uid: response.data,           // 传递拟注册的uid
          redirect: redirectPath        // 传递重定向地址
        }
      });
    } else {
      ElMessage.error(response.message || '登录失败');
    }
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error('登录失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

// 切换到密码登录
const switchToPasswordLogin = () => {
  router.push({
    path: '/sign/password',
    query: route.query // 保持重定向参数
  });
};

// 验证码验证成功后的 方法
const captchaSuccessOn = (uid: string) => {
  handleCaptchaSuccess(uid);
}
</script>

<template>
  <div class="email-login">
    <el-form @submit.prevent="handleLogin">
      <!-- 邮箱输入 -->
      <el-form-item>
        <el-input
            v-model="email"
            placeholder="请输入邮箱"
            size="large"
            class="custom-input"
            :autofocus="false"
        >
          <template #prefix>
            <el-icon class="input-icon">
              <Message/>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 验证码输入 -->
      <el-form-item>
        <div class="verification-code-container">
          <el-input
              v-model="verificationCode"
              placeholder="请输入验证码"
              size="large"
              class="custom-input"
              :autofocus="false"
          >
            <template #prefix>
              <el-icon class="input-icon">
                <Key/>
              </el-icon>
            </template>
          </el-input>
          <el-button
              type="primary"
              :disabled="countdown > 0"
              @click="sendVerificationCode"
              size="large"
              class="send-code-btn"
          >
            {{ countdown > 0 ? `${countdown}s后重新发送` : '发送验证码' }}
          </el-button>
        </div>
      </el-form-item>

      <!-- 登录按钮 -->
      <el-form-item>
        <el-button
            type="primary"
            :loading="loading"
            @click="handleLogin"
            class="login-btn"
        >
          登录 / 注册
        </el-button>
      </el-form-item>

      <!-- 切换到密码登录 -->
      <div class="switch-login-type">
        <a @click="switchToPasswordLogin">
          <el-icon>
            <Lock/>
          </el-icon>
          使用密码登录
        </a>
      </div>
    </el-form>
  </div>

  <!-- 滑块验证码弹窗 -->
  <SliderCaptcha
      v-if="showCaptchaDialog"
      :on-success="captchaSuccessOn"
      :on-close="closeCaptcha"
  />


</template>

<style scoped>
.email-login {
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

.verification-code-container {
  display: flex;
  gap: 10px;
}

.verification-code-container .el-input {
  flex: 1;
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

.send-code-btn {
  width: 130px;
  white-space: nowrap;
  border-radius: 8px;
  transition: all 0.3s ease;
  user-select: none;
  font-weight: 500;
  letter-spacing: 0.5px;
  height: 48px;
  background-color: #000;
  color: #fff;
  border: none;
}

.send-code-btn:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  background-color: #222;
}

.send-code-btn:disabled {
  background: #f0f0f0;
  color: #909399;
  border: none;
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

.switch-login-type {
  text-align: center;
  margin-top: 24px;
  user-select: none;
}

.switch-login-type a {
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

.switch-login-type a:hover {
  color: #000;
  background-color: rgba(0, 0, 0, 0.08);
}

.switch-login-type i {
  font-size: 14px;
}

/* 验证码弹窗样式 */
:deep(.captcha-dialog .el-dialog__header) {
  margin: 0;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.captcha-dialog .el-dialog__body) {
  padding: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>